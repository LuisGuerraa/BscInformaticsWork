package pt.isel.ls.result;

import java.util.function.Function;

public class Property<T> {

    private final Function<T, String> func;
    private final Function<T, String> linkFunc;

    public Property(Function<T, String> func) {
        this.func = func;
        this.linkFunc = null;
    }

    public Property(Function<T, String> func, Function<T, String> linkFunc) {
        this.func = func;
        this.linkFunc = linkFunc;
    }

    boolean hasLink() {
        return linkFunc != null;
    }

    Function<T, String> getFunc() {
        return func;
    }

    Function<T, String> getLinkFunc() {
        return linkFunc;
    }
}
