package pt.isel.ls.resultview;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import pt.isel.ls.node.HtmlTree;
import pt.isel.ls.result.Result;

public class CommonResultView extends ResultViewer {
    private List<PathLink> linkList;
    private List<Result> results;
    private String title;

    public CommonResultView(String title, List<PathLink> linkList, List<Result> results) {
        super(results);
        this.title = title;
        this.linkList = linkList;
        this.results = results;
    }

    public CommonResultView(String title, List<PathLink> linkList, Result result) {
        this(title, linkList, Collections.singletonList(result));
    }

    public CommonResultView(String title, Result result) {
        this(title, Collections.emptyList(), result);
    }


    @Override
    public void printPlain(PrintStream printStream) {
        printStream.println(title);
        for (Result result : results) {
            printStream.println();
            printStream.println(result.getHeader());
            for (String line : result.createPlainList()) {
                printStream.println(line);
            }
        }
    }

    @Override
    public void printHtml(PrintStream printStream) {
        HtmlTree tree = new HtmlTree(title);
        for (Result result : results) {
            tree.addHeader2(result.getHeader());
            tree.addToTree(result.getBody());
        }
        for (PathLink pathLink : linkList) {
            tree.addHeader2WithLink(pathLink.getLink(), pathLink.getTitle());
        }

        tree.addHeader2WithLink("/", "Home");
        tree.getHtml().writeTo(printStream);
    }

}
