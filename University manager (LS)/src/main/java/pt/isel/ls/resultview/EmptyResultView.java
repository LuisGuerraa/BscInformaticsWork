package pt.isel.ls.resultview;

import java.io.PrintStream;
import java.util.Collections;

import pt.isel.ls.node.HtmlTree;

public class EmptyResultView extends ResultViewer {

    public EmptyResultView() {
        super(Collections.emptyList());
    }

    @Override
    public void printHtml(PrintStream printStream) {
        HtmlTree tree = new HtmlTree("No Results for the inserted command.");
        tree.addHeader2WithLink("/", "Home");
        tree.getHtml().writeTo(printStream);
    }

    @Override
    public void printPlain(PrintStream printStream) {
    }
}
