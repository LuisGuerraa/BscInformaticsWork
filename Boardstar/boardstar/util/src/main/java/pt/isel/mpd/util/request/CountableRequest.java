package pt.isel.mpd.util.request;

import java.io.InputStream;
import java.io.Reader;

public class CountableRequest extends AbstractRequest {
    private int count;
    private AbstractRequest req;

    public CountableRequest(AbstractRequest req) {
        this.req = req;
        this.count = 0;
    }

    public int getCount() { return count; }

    @Override
    public InputStream getStream(String path) {
        count += 1;
        return req.getStream(path);
    }

    @Override
    public Reader getReader(String path) {
        return null;
    }
}
