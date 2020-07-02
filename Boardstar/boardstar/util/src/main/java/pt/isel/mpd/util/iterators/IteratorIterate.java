package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.function.UnaryOperator;

public class IteratorIterate<T> implements Iterator<T> {
    private final T seed;
    private final UnaryOperator<T> acc;
    private T curr;

    public IteratorIterate(T seed, UnaryOperator<T> acc) {
        this.seed = seed;
        this.acc = acc;
        curr = seed;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        T temp = curr;
        curr = acc.apply(temp);
        return temp;
    }
}
