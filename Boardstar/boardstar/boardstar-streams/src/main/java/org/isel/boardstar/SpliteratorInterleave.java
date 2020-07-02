package org.isel.boardstar;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class SpliteratorInterleave<T> extends Spliterators.AbstractSpliterator {
    private Spliterator<T> src;
    private Spliterator<T> other;

    protected SpliteratorInterleave(Spliterator<T> src, Spliterator<T> other) {
        super(src.estimateSize() + other.estimateSize(), src.characteristics() & other.characteristics());
        this.src = src;
        this.other = other;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        Spliterator<T> sp = src;
        if (sp.tryAdvance(action)) {
            src = other;
            other = sp;
            return true;
        }
        return other.tryAdvance(action);
    }
}
