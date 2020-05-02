package pt.isel.ls.resultview;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.isel.ls.result.Result;

public abstract class ResultViewer {

    private final List<Result> results;
    private final Map<String, String> headers;
    private List<String> infos;

    public ResultViewer(List<Result> results) {
        this.results = results;
        headers = new HashMap<>();
        infos = new LinkedList<>();
    }

    public abstract void printPlain(PrintStream printStream);

    public abstract void printHtml(PrintStream printStream);

    public List<Result> getResults() {
        return results;
    }

    public ResultViewer addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Set<Map.Entry<String,String>> getEntrySet() {
        return headers.entrySet();
    }

    public String getMediaType() {
        return "text/html";
    }

    public ResultViewer addInfo(String info) {
        infos.add(info);
        return this;
    }

    public List<String> getInfos() {
        return infos;
    }
}
