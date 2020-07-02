package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorLimit<T> implements Iterator<T> {
    private final Iterator<T> src;
    private final int limit;
    private int idx;

    public IteratorLimit(Iterable<T> src, int limit) {
        this.src = src.iterator();
        this.limit = limit;
        idx = 0;
    }

    @Override
    public boolean hasNext() {
        return idx < limit && src.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        idx++;
        return src.next();
    }
}
