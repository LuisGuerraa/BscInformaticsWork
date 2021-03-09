import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferQueue<E> {

    private final Lock lock = new ReentrantLock();

    private class Message {
        E message;
        final Condition hasTaken;
        boolean toAck;
        boolean done;

        //put
        Message(E message) {
            this(message,false,null);
        }
        //transfer
        Message(E message,boolean toAck,Condition hasTaken) {
            this.message = message;
            this.toAck = toAck;
            this.hasTaken = hasTaken;
            done = false;
        }
    }


    private class TakeRequest {
        Message message;
        final Condition hasBeenTransferred;
        boolean done;

        TakeRequest(Condition hasTransfered) {
            this.hasBeenTransferred = hasTransfered;
            message = null;
            done = false;
        }
    }

    private final LinkedList<TakeRequest> takeRequests = new LinkedList<>(); // list of requests
    private final LinkedList<Message> pendingMessages = new LinkedList<>();

    private void updateStateAfterPut(Message message) { pendingMessages.addLast(message); }

    public void put(E message) {
        lock.lock();
        try {
            Message mes = new Message(message);
            if(!takeRequests.isEmpty()) {

                TakeRequest takeRequest = takeRequests.poll();
                takeRequest.message = mes;
                takeRequest.done = true;
                takeRequest.hasBeenTransferred.signal();

            } else {
                updateStateAfterPut(mes);
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean transfer(E message, long timeout) throws InterruptedException {
        lock.lock();
        try {
            if(!takeRequests.isEmpty()) {
                Message mes = new Message(message);
                TakeRequest takeRequest = takeRequests.poll();
                takeRequest.message = mes;
                takeRequest.done = true;
                takeRequest.hasBeenTransferred.signal();
                return true;
            }

            Message mes = new Message(message,true,lock.newCondition());
            pendingMessages.addLast(mes);

            boolean isTimed = timeout > 0;
            long nanosTimeout = isTimed ? TimeUnit.MILLISECONDS.toNanos(timeout) : 0L;

            do {
                if(isTimed && nanosTimeout <= 0L) {
                    pendingMessages.remove(mes);
                    return false;
                }
                try { nanosTimeout = mes.hasTaken.awaitNanos(nanosTimeout);}
                catch (InterruptedException e) {
                    if(mes.done) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    pendingMessages.remove(mes);
                    throw e;
                }
            } while(!mes.done);

            return true;
        } finally {
            lock.unlock();
        }
    }


    public E take(long timeout) throws InterruptedException {
        lock.lock();
        try {
            if (takeRequests.isEmpty() && !pendingMessages.isEmpty()) {
                return takeSideEffect();
            }

            TakeRequest takeRequest = new TakeRequest(lock.newCondition());
            takeRequests.addLast(takeRequest);

            boolean isTimed = timeout > 0;
            long nanosTimeout = isTimed ? TimeUnit.MILLISECONDS.toNanos(timeout) : 0L;
            do {
                if (isTimed && nanosTimeout <= 0L) {
                    takeRequests.remove(takeRequest);
                    return null;
                }
                try { nanosTimeout = takeRequest.hasBeenTransferred.awaitNanos(nanosTimeout);}
                catch (InterruptedException e) {
                    if (takeRequest.done) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    takeRequests.remove(takeRequest);
                    throw e;
                }
            } while (!takeRequest.done) ;
            return takeRequest.message.message;
        } finally {
            lock.unlock();
        }

    }


    private E takeSideEffect() {
        Message message = pendingMessages.poll();
        if(message.toAck) {
            message.done = true;
            message.hasTaken.signal();
        }
        return message.message;
    }
}
