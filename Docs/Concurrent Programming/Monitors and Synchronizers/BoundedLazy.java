package serie1;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class BoundedLazy<E> {

    private final Lock monitor;
    private final Condition condition;
    private final Supplier<E> supplier;

    private State state = State.UNCREATED;
    private E value;
    private final int lives;
    private int current_lives;
    private Exception error;

    public BoundedLazy(Supplier<E> supplier, int lives) {
        this.supplier = supplier;
        this.lives = lives;
        current_lives = lives;
        monitor = new ReentrantLock();
        condition = monitor.newCondition();
    }

    private enum State {
        UNCREATED, CREATING, CREATED, ERROR
    }

    private boolean canAcquire() {
        return state == State.CREATED;
    }

    private Optional<E> acquireSideEffect() {
        if (--current_lives == 0) state = State.UNCREATED;
        return Optional.of(value);
    }

    private boolean needsToBeCalculated() {
        return state == State.UNCREATED;
    }

    private void needToCalculateSideEffect() {
        state = State.CREATING;
    }

    private boolean errorOccurred() {
        return state == State.ERROR;
    }


    public Optional<E> get(long timeout) throws Exception {
        monitor.lock();
        try {
            if (canAcquire())
                return acquireSideEffect();

            if (!needsToBeCalculated()) {
                boolean isTimed = timeout >= 0;
                long nanosTimeout = isTimed ? TimeUnit.MILLISECONDS.toNanos(timeout) : 0L;
                do {
                    if (isTimed) {
                        if (nanosTimeout <= 0) {
                            return Optional.empty();
                        }
                        nanosTimeout = condition.awaitNanos(nanosTimeout);
                    } else {
                        condition.await();
                    }

                    if (errorOccurred())
                        throw error;

                    if (needsToBeCalculated())
                        break; // Value needs to be calculated

                    if (canAcquire())
                        return acquireSideEffect();

                } while (true);
            }
            // Value ready to be calculated
            needToCalculateSideEffect();
        } finally {
            monitor.unlock();
        }

        E v = null;
        Exception ex = null;
        try {
            v = supplier.get();
        } catch (Exception e) {
            ex = e;
        }
        monitor.lock();
        try {
            if (ex == null) { // No exception
                value = v;
                current_lives = lives - 1;
                state = State.CREATED;
                condition.signalAll();
                return Optional.of(value);
            }
            else {            // Exception when calculating
                error = ex;
                state = State.ERROR;
                condition.signalAll();
                throw error;
            }
        } finally {
            monitor.unlock();
        }
    }


}
