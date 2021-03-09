

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Exchanger<T> {

    private Lock lock = new ReentrantLock();

    private class Exchanging { //point where threads are exchanging their data
        T data;
        final Condition exchanged;
        boolean exchangeFinished;

        Exchanging(T data, Condition exchanged) {
            this.data = data;
            this.exchanged = exchanged;
            exchangeFinished = false;
        }
    }

    private final LinkedList<Exchanging> numberOfExchanges = new LinkedList<>(); //all exchanges made throughout

    private T exchangeSideEffect(T mydata) { // side effect of an exchange
        Exchanging exp = numberOfExchanges.element();
        T hisData = exp.data;
        exp.data = mydata;
        exp.exchangeFinished = true;
        numberOfExchanges.remove();
        exp.exchanged.signal();

        return hisData;
    }

    public Optional<T> exchange(T mydata, long timeout) throws InterruptedException {
        lock.lock();
        try {
            if (numberOfExchanges.size() > 0) {
                T hisData = exchangeSideEffect( mydata);
                return Optional.of(hisData);
            }
            Exchanging exp = new Exchanging(mydata,lock.newCondition());
            boolean isTimed = timeout > 0;
            long nanosTimeout = isTimed ? TimeUnit.MILLISECONDS.toNanos(timeout) : 0L;

            do {
                if(isTimed && nanosTimeout <= 0L) {
                    numberOfExchanges.remove();
                    return Optional.empty();
                }
                try {
                    nanosTimeout = exp.exchanged.awaitNanos(nanosTimeout);
                }
                catch (InterruptedException e) {
                    if(exp.exchangeFinished) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    numberOfExchanges.remove();
                    throw e;
                }
            } while (!exp.exchangeFinished);

            T hisData = exp.data;
            return Optional.of(hisData);

        } finally {
            lock.unlock();
        }
    }


}
