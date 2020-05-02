package pt.isel.ls.resultview.form;

import java.util.List;

import pt.isel.ls.node.Element;

public class FormRadio implements Form {

    private final String description;
    private final String valueName;
    private final List<String> values;

    public FormRadio(String description, String valueName, List<String> values) {

        this.description = description;
        this.valueName = valueName;
        this.values = values;
    }

    @Override
    public void setForm(Element form) {
        form.addLabel(description, description);
        for (String value : values) {
            form.addInput("radio", valueName).addAttributes("value", value).addText(value);
        }
        form.addBr();
    }
}
