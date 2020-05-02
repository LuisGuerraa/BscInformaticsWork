package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;

public class PostGroupsAcrSemNumGnoForm extends CommandHandler {
    private final String num = "num";
    private final String sem = "sem";
    private final String acr = "acr";
    private final String gno = "gno";

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acrCourse = parameter.getValue(acr);
        String semester = parameter.getValue(sem);
        String classId = parameter.getValue(num);
        int gnumber = Integer.parseInt(parameter.getValue(gno));
        String groupInfo = "G" + gnumber + "-" + classId + "-" + acrCourse + "-" + semester;
        return new FormView(
                "Add Student to Group " + groupInfo,
                Template.getStudentGroup(acrCourse,semester,classId,gnumber),
                List.of(
                        new FormText("Student Number", "numStu")));
    }

    @Override
    public String[] getArguments() {
        return new String[]{num,sem,acr,gno};
    }

    @Override
    public String getDescription() {
        return "";
    }
}
