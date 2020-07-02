package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T> {
    private final Iterator<T> src;
    private final Predicate<T> pred;
    private T elem = null;

    public IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.src = src.iterator();
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if (elem == null){
            if (src.hasNext()){
                elem = src.next();
                if (pred.test(elem))
                    return true;
            }
            return false;
        }
        return pred.test(elem);
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        T aux = elem;
        elem = null;
        return aux;
    }
}
