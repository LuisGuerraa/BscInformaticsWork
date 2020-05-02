package pt.isel.ls.response;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import pt.isel.ls.resultview.ResultViewer;

public class Response {
    private final int code;
    private final Charset charset = Charset.forName("UTF-8");
    private final ResultViewer writer;

    Response(HttpStatusCode code, ResultViewer writer) {
        this.code = code.valueOf();
        this.writer = writer;
    }

    Response(HttpStatusCode code) {
        this(code,null);
    }

    void send(HttpServletResponse resp) throws IOException {
        resp.setStatus(code);
        if (writer == null) {
            return;
        }
        for (Map.Entry<String, String> entry : writer.getEntrySet()) {
            resp.addHeader(entry.getKey(), entry.getValue());
        }
        String type = String.format("%s;charset=%s",writer.getMediaType(), charset.name());
        resp.setContentType(type);
        PrintStream ps = new PrintStream(resp.getOutputStream());
        writer.printHtml(ps);
        ps.close();
    }
}
