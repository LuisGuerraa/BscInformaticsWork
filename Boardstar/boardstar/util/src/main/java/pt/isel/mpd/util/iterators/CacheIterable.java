package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class CacheIterable<T> implements Iterable<T> {
    private Iterable<T> iterable;
    private Iterator<T> srcIt;
    private List<T> cache;
    private boolean full;

    public CacheIterable(Iterable<T> src) {
        this.iterable = src;
        cache = new LinkedList<>();
        full = false;
    }

    @Override
    public Iterator<T> iterator() {
        if(full) return cache.iterator();
        else {
            return new Iterator<T>() {
                int counter = 0;
                boolean flag = false;

                @Override
                public boolean hasNext() {
                    if (srcIt == null) srcIt = iterable.iterator();
                    if (flag) return true;
                    if (srcIt.hasNext()) {
                        flag = true;
                        return true;
                    }
                    full = true;
                    return false;
                }

                @Override
                public T next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    flag = false;
                    if (counter >= cache.size()) {
                        T aux = srcIt.next();
                        cache.add(aux);
                        counter++;
                        return aux;
                    }
                    return cache.get(counter++);
                }
            };
        }
    }
}
