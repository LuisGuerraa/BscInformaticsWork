package pt.isel.ls.resultview;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import pt.isel.ls.Template;
import pt.isel.ls.node.Element;
import pt.isel.ls.node.HtmlTree;

public class HomeResultView extends ResultViewer {

    private static final List<String> paths =
            List.of("Courses", "Programmes", "Teachers", "Students");
    private static final List<String> links =
            List.of(Template.COURSES, Template.PROGRAMMES, Template.TEACHERS, Template.STUDENTS);

    public HomeResultView() {
        super(Collections.emptyList());
    }

    public Element getView() {
        HtmlTree tree = new HtmlTree("App Home");
        tree.addHeader1("Home");
        for (int i = 0; i < paths.size(); i++) {
            tree.addHeader2WithLink(links.get(i), paths.get(i));
        }
        return tree.getHtml();
    }

    @Override
    public void printPlain(PrintStream printStream) {
    }

    @Override
    public void printHtml(PrintStream printStream) {
        getView().writeTo(printStream);
    }

}
