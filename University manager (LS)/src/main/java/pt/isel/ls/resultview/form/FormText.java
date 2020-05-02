package pt.isel.ls.resultview.form;

import pt.isel.ls.node.Element;

public class FormText implements Form {
    private String description;
    private String value;

    public FormText(String description, String value) {
        this.description = description;
        this.value = value;
    }

    @Override
    public void setForm(Element form) {
        form.addLabel(description, description);
        form.addInput("text", value);
        form.addBr();
    }

}
