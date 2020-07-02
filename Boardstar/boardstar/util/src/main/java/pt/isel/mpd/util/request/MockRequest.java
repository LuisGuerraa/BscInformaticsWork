package pt.isel.mpd.util.request;

import java.io.InputStream;
import java.io.Reader;

public class MockRequest extends AbstractRequest {
    @Override
    public InputStream getStream(String path) {
        path = path.replace('&', '_')
                .replace('/', '_')
                .replace(',', '_')
                .replace('=', '_')
                .replace('?', '_')
                .substring(35);
        path += ".txt";

        return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
    }

    @Override
    public Reader getReader(String path) {
        return null;
    }
}