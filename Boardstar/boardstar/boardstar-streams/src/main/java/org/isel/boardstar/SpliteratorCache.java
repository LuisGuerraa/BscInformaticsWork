package org.isel.boardstar;

import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class SpliteratorCache<T> extends Spliterators.AbstractSpliterator {
    private Spliterator<T> src;
    private List<T> cache;
    private int index;

    protected SpliteratorCache(Spliterator<T> src, LinkedList<T> cache) {
        super(src.estimateSize(), src.characteristics());
        this.cache = cache;
        this.src = src;
        index = 0;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        if (index < cache.size()) {
            T value = cache.get(index);
            index++;
            action.accept(value);
            return true;
        }
        return src.tryAdvance(value -> {
            cache.add(value);
            index++;
            action.accept(value);
        });
    }
}
