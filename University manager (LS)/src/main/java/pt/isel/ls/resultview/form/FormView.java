package pt.isel.ls.resultview.form;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import pt.isel.ls.node.Element;
import pt.isel.ls.node.HtmlTree;
import pt.isel.ls.resultview.ResultViewer;

public class FormView extends ResultViewer {
    private String title;
    private String url;
    private List<Form> forms;

    public FormView(String title, String urlSubmit, List<Form> forms) {
        super(Collections.emptyList());
        url = urlSubmit;
        this.title = title;
        this.forms = forms;
    }

    @Override
    public void printPlain(PrintStream printStream) {
    }

    @Override
    public void printHtml(PrintStream printStream) {
        HtmlTree tree = new HtmlTree(title);
        Element form = new Element("p").addForm("post",url);
        for (Form f : forms) {
            f.setForm(form);
        }
        form.addInputWithValue("submit","Submit");
        tree.addToTree(form);
        for (String info : getInfos()) {
            tree.addHeader2(info).addAttributes("style", "color:red");
        }

        tree.addHeader2WithLink("/", "Home");
        tree.getHtml().writeTo(printStream);
    }

}
