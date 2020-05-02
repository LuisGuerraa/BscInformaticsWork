package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;


public class PostCoursesAcrForm extends CommandHandler {
    private final String acr = "acr";

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acronym = parameter.getValue(acr);
        return new FormView(
                "Add a new Class to " + acronym,
                Template.getCoursesAcrClasses(acronym),
                List.of(
                        new FormText("Semester Identifier", "sem"),
                        new FormText("Class Number", "num")));
    }

    @Override
    public String[] getArguments() {
        return new String[] {acr};
    }

    @Override
    public String getDescription() {
        return "Form of " + Template.COURSES_ACR_CLASSES;
    }
}
