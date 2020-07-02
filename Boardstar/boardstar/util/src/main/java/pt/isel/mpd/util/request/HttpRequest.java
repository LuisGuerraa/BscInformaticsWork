package pt.isel.mpd.util.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public class HttpRequest extends AbstractRequest {
    @Override
    public InputStream getStream(String path) {
        try {
            return new URL(path).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reader getReader(String path) {
        return null;
    }
}
