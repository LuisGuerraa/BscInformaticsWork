package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormRadio;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;

public class PostProgrammesPid extends CommandHandler {
    public static final String pid = "pid";

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String programme = parameter.getValue(pid);
        return new FormView(
                "Add a Course to " + programme, Template.getProgrammesPidCourses(programme),
                List.of(
                        new FormText("Course Acronym", "acr"),
                        new FormRadio("Course Mandatory","mandatory",
                                List.of("True", "False")),
                        new FormText("List of Curricular Semesters(comma separated)",
                                "semesters")));
    }

    @Override
    public String[] getArguments() {
        return new String[]{pid};
    }

    @Override
    public String getDescription() {
        return "";
    }
}
