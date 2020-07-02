package pt.isel.mpd.util.request;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import pt.isel.mpd.util.AsyncRequest;

import java.util.concurrent.CompletableFuture;

public class AsyncHttpRequest implements AsyncRequest {
    private AsyncHttpClient ahc = Dsl.asyncHttpClient();

    @Override
    public CompletableFuture<String> getBody(String path) {
        return ahc
                .prepareGet(path)
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBody);
    }

    @Override
    public void close() throws Exception {
        if (ahc != null) ahc.close();
    }
}
