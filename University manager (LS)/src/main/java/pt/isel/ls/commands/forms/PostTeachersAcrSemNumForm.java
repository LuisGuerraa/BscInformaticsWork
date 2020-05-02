package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;

public class PostTeachersAcrSemNumForm extends CommandHandler {
    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num";

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acronym = parameter.getValue(acr);
        String semester = parameter.getValue(sem);
        String classNum = parameter.getValue(num);
        String classInfo = classNum + "-" + acronym + "-" + semester;
        return new FormView(
                "Add a Teacher to " + classInfo,
                Template.getTeacherClass(acronym,semester,classNum),
                List.of(
                        new FormText("Teacher Number", "numDoc")));
    }

    @Override
    public String[] getArguments() {
        return new String[] {acr,sem,num};
    }

    @Override
    public String getDescription() {
        return "";
    }
}
