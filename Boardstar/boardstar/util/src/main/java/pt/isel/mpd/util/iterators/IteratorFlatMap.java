package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class IteratorFlatMap<T, R> implements Iterator<R> {
    private final Iterator<T> src;
    private final Function<T, Iterable<R>> mapper;
    private R elem = null;
    private Iterator<R> rIter = null;

    public IteratorFlatMap(Iterable<T> src, Function<T, Iterable<R>> mapper) {
        this.src = src.iterator();
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        if (elem != null) return true;
        if (rIter != null && rIter.hasNext()){
            elem = rIter.next();
            return true;
        }
        while (src.hasNext()){
            rIter = mapper.apply(src.next()).iterator();
            if (rIter.hasNext()) {
                elem = rIter.next();
                return true;
            }
        }
        return false;
    }

    @Override
    public R next() {
        if (elem == null && !hasNext()) throw new NoSuchElementException();
        R aux = elem;
        elem = null;
        return aux;
    }
}
