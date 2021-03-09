import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BroadcastBox <E>{

    private final Lock monitor = new ReentrantLock();
    private final Condition cond = monitor.newCondition();

    private class sharedObj{
        boolean deliverToAllReceivingThreads = false; //done
        int waiters ;
        E message;

        public sharedObj(){
            waiters = 1;
        }
    }

    private sharedObj reqQueue = null;
    private boolean signalState;

    public BroadcastBox (boolean initialState){
        initialState = false;
        signalState = initialState;
    }

    public sharedObj addWaiter() {
        if(reqQueue==null) reqQueue = new sharedObj();
        else reqQueue.waiters++;

        return reqQueue;

    }
    public void removeWaiter(){
        if(--reqQueue.waiters== 0) reqQueue =null;
        --reqQueue.waiters;
    }

    public int deliverToAll(E message){
        monitor.lock();
        try {
            signalState = true;
            if(reqQueue!= null){
                reqQueue.deliverToAllReceivingThreads=true;
                reqQueue.message=message;
                reqQueue = null;
                cond.signalAll();
                return reqQueue.waiters;
            }
        }finally {
            monitor.unlock();
        }
        return 0;
    }
    public Optional<E> receive(long timeout) throws InterruptedException{
        monitor.lock();
        try {
            if(signalState){
                return Optional.of(reqQueue.message);
            }
            TimeoutHolder th = new TimeoutHolder(timeout);
            do {
                addWaiter();
                if ((timeout = th.value()) == 0) {
                    removeWaiter();
                    return Optional.empty();
                }
                monitor.wait(timeout);

            } while (!reqQueue.deliverToAllReceivingThreads);
            removeWaiter();

        }finally {
            monitor.unlock();
        }
        return Optional.empty();
    }
}
