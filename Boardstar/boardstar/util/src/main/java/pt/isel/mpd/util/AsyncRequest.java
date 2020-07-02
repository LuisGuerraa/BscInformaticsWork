package pt.isel.mpd.util;

import java.util.concurrent.CompletableFuture;

public interface AsyncRequest extends AutoCloseable {
    CompletableFuture<String> getBody(String path);
}
