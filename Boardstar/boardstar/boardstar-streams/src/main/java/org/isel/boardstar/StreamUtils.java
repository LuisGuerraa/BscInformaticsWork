package org.isel.boardstar;

import org.isel.boardstar.model.Category;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
    public static <T> Supplier<Stream<T>> cache(Stream<T> src) {
        Spliterator<T> iter = src.spliterator();
        LinkedList<T> list = new LinkedList<>();
        return () -> {
            Spliterator<T> splitCache = new SpliteratorCache<>(iter, list);
            return StreamSupport.stream(splitCache, false);
        };
    }

    public static <T> Stream<T> interleave(Stream<T> src, Stream<T> other) {
        Spliterator<T> iterSrc = src.spliterator();
        Spliterator<T> iterOther = other.spliterator();
        Spliterator<T> splitInterleave = new SpliteratorInterleave<>(iterSrc, iterOther);
        return StreamSupport.stream(splitInterleave, false);
    }

    public static <T> Stream<T> intersection(Stream<T> src, Stream<T> other) {
        Supplier<Stream<T>> otherCache = cache(other);
        Stream<T> res = src.filter(srvVal -> otherCache.get().anyMatch(srvVal::equals));
        return res.distinct();
    }

    public static <T> Stream<T> headersContent(Stream<T> headers, Stream<Stream<T>> contents) {
        /*Stream.Builder<String> result = Stream.builder();

        Iterator<String> headersIo = headers.iterator();
        Iterator<Stream<String>> contentsIo = contents.iterator();

        while (headersIo.hasNext()) {
            result.add(headersIo.next());
            Iterator<String> eachContentIo = contentsIo.next().iterator();
            while(eachContentIo.hasNext()) {
                result.add(eachContentIo.next());
            }
        }

        return result.build();
         */

        return null;
    }
}
