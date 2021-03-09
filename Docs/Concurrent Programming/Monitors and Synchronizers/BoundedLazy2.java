import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;


public class BoundedLazy<E> {
    Supplier<E> sup;
    final int lives;
    private final Lock monitor = new ReentrantLock();
    private final Condition cond = monitor.newCondition();
    Exception sendError = null;

    int numberOfUses = 0;
    E value = null;
    boolean isBeingCalculated = false;

    enum states { //possible states for value
        readyToBeCalculated,
        Calculated,
        notCalculated,
        empty
    }


    public BoundedLazy(Supplier<E> supplier, int lives) {
        sup = supplier;
        this.lives = lives;
    }

    public Optional<E> get(long timeout) throws Exception {

        states currentState = auxiliar(timeout);

        if (currentState == states.Calculated) {
            return Optional.of(value);
        }

        if (currentState == states.notCalculated) {
            isBeingCalculated = true;

            try {
                value = sup.get();
                numberOfUses = 1; // reset 
                cond.signalAll(); // signal all blocked threads that value has been calculated
                return Optional.of(value);

            } catch (Exception e) {
                cond.signalAll();
                sendError = e;
            }

        }

        return Optional.empty();

    }

    public states auxiliar(long timeout) throws Exception {

        monitor.lock();
        try {
            if (value != null && numberOfUses < lives) {
                numberOfUses++;
                return states.Calculated;
            }

            if (isBeingCalculated) {
                TimeoutHolder th = new TimeoutHolder(timeout);
                do {
                    if (th.isTimed()) {
                        if ((timeout = th.value()) <= 0)
                            return states.empty;        // timeout
                        monitor.wait(timeout);
                    } else
                        monitor.wait();
                    if (sendError != null) throw sendError;

                } while (value == null); // not calculated

                return states.Calculated;
            }
            return states.readyToBeCalculated;

        } catch (InterruptedException e) {
            throw e;
        } finally {
            monitor.unlock();
        }

    }

}