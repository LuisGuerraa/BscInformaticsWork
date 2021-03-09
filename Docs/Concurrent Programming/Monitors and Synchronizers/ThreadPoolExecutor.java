import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadPoolExecutor {

    private Lock lock = new ReentrantLock();
    private int maxPoolSize;
    private long keepAliveTime = 0;
    private int threadCount = 0;
    private LinkedList<PoolThread> availablePool;
    private LinkedList<WaitingResult> waitingWork =  new LinkedList<>();
    private boolean isShuttingdown = false;
    private Condition shutDownOver = lock.newCondition();

    private boolean shutDownDone = false;

    private class State{

    }

    public ThreadPoolExecutor(int maxPoolSize, long keepAliveTime) {
        availablePool = new LinkedList<>();
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
    }

    private boolean canCreateThread() { return threadCount < maxPoolSize; }

    public <T> Result<T> execute(Callable<T> command) throws InterruptedException {
        lock.lock();
        try {
            if(isShuttingdown)
                throw new RejectedExecutionException();
            if(availablePool.size() > 0) {
                PoolThread worker = availablePool.poll();
                ResultObject<T> result = new ResultObject<>();
                worker.giveWork(result,command);
                worker.work.signal();

                return result;
            } else if(canCreateThread()) {
                PoolThread newWorker = new PoolThread();
                ++threadCount;
                ResultObject<T> result = new ResultObject<>();
                newWorker.giveWork(result, command);
                newWorker.start();

                return result;
            }
            WaitingResult<T> result = new WaitingResult<>(command);
            waitingWork.addLast(result);
            return result;

        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            isShuttingdown = true;
            for (PoolThread thread: availablePool) {
                thread.work.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean awaitTermination(long timeout) throws InterruptedException {
        lock.lock();
        try {
            boolean isTimed = timeout > 0;
            long nanosTimeout = isTimed ? TimeUnit.MILLISECONDS.toNanos(timeout) : 0L;

            while(!shutDownDone) {
                if (isTimed && nanosTimeout <= 0L) {
                    return false;
                }
                nanosTimeout = shutDownOver.awaitNanos(nanosTimeout);
            }
            return true;

        } finally {
            lock.unlock();
        }
    }
    public enum Possiblestates{
        NotComputed,
        isComputing,
        Computed,
        Cancelled,
        Exception

    }

    private class ResultObject<T> implements Result<T> {

        Optional<T> result = Optional.empty();
        Condition hasResult = lock.newCondition();
        Possiblestates state = Possiblestates.Cancelled;
        Exception callableException;

        @Override
        public boolean isComplete() {
            lock.lock();
            try {
                return state == Possiblestates.Computed;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean tryCancel() {
            lock.lock();
            try {
                if (state == Possiblestates.NotComputed) {
                    waitingWork.remove(this);
                    state = Possiblestates.Cancelled;
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Optional<T> get(int timeout) throws Exception {
            lock.lock();
            try {
                if(state == Possiblestates.Exception) {
                    throw callableException;
                }
                else if(state == Possiblestates.Cancelled) {
                    throw new CancellationException();
                }
                else if(state == Possiblestates.Computed) {
                    return result;
                }
                boolean isTimed = timeout > 0;
                long nanosTimeout = isTimed ? TimeUnit.MILLISECONDS.toNanos(timeout) : 0L;

                while(state == Possiblestates.isComputing || state == Possiblestates.NotComputed) {
                    if(isTimed && nanosTimeout <= 0L) {
                        state = Possiblestates.Cancelled;
                        return Optional.empty();
                    }
                    nanosTimeout = hasResult.awaitNanos(nanosTimeout);
                }

                return result;

            } finally {
                lock.unlock();
            }
        }
    }

    private class WaitingResult<T> extends ResultObject<T> {
        Callable<T> command;

        WaitingResult(Callable<T> command) {
            super();
            this.command = command;
        }
    }

    private class PoolThread extends Thread {

        private ResultObject result;
        private Callable command;
        private boolean hasWorktoDo = false;
        private Condition work = lock.newCondition();

        private boolean checkWork() {
            lock.lock();
            try {
                if(hasWorktoDo) {
                    return true;
                }

                if (!waitingWork.isEmpty()) {
                    WaitingResult temp = waitingWork.poll();
                    this.giveWork(temp, temp.command);
                    return true;
                }

                availablePool.addLast(this);
                boolean isTimed = keepAliveTime > 0;
                long nanosTimeout = isTimed ? TimeUnit.MILLISECONDS.toNanos(keepAliveTime) : 0L;

                do {
                    try {
                        if(isShuttingdown) {
                            availablePool.remove(this);
                            --threadCount;

                            if(threadCount == 0) {
                                shutDownDone = true;
                                shutDownOver.signal();
                            }
                            return false;
                        }
                        if (isTimed) {
                            if(nanosTimeout <= 0L) {
                                availablePool.remove(this);
                                --threadCount;
                                if(threadCount == 0) {
                                    shutDownDone = true;
                                    shutDownOver.signal();
                                }
                                return false;
                            }
                            nanosTimeout = work.awaitNanos(nanosTimeout);
                        } else {
                            work.await();
                        }
                    } catch (InterruptedException e) {
                        if(hasWorktoDo) {
                            Thread.currentThread().interrupt();
                            return true;
                        }
                        return false;
                    }
                } while(!hasWorktoDo);

                return true;

            } finally {
                lock.unlock();
            }
        }

        void doWork() {
            Exception callableException = null;
            Optional commandResult = null;
            try {
                commandResult = Optional.of(command.call());
            } catch (Exception e) {
                callableException = e;
            }
            lock.lock();
            try {
                if (callableException == null) {
                    result.result = commandResult;
                    result.state = Possiblestates.Computed;
                    result.hasResult.signal();
                } else {
                    result.callableException = callableException;
                    result.state = Possiblestates.Exception;
                    result.hasResult.signal();
                }

                hasWorktoDo = false;

            } finally {
                lock.unlock();
            }
        }

        void giveWork(ResultObject result, Callable command) {
            this.result = result;
            result.state = Possiblestates.isComputing;
            this.command = command;
            hasWorktoDo = true;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " is running ");
            while (checkWork()) {
                doWork();
            }
        }
    }
}