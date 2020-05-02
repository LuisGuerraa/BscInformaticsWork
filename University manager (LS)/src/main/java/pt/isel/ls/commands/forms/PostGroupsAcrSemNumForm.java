package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;

public class PostGroupsAcrSemNumForm extends CommandHandler {
    private final String num = "num";
    private final String sem = "sem";
    private final String acr = "acr";

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acrCourse = parameter.getValue(acr);
        String semester = parameter.getValue(sem);
        String classId = parameter.getValue(num);
        String classNum = classId + "-" + acrCourse + "-" + semester;
        return new FormView(
                "Add a new Group to " + classNum,
                Template.getGroupsClass(
                        acrCourse, semester, classId),
                List.of(
                        new FormText("Student Number", "numStu")));
    }

    @Override
    public String[] getArguments() {
        return new String[]{acr,sem,num};
    }

    @Override
    public String getDescription() {
        return "";
    }
}
