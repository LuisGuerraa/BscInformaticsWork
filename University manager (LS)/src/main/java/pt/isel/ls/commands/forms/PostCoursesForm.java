package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;

public class PostCoursesForm extends CommandHandler {

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        return new FormView(
                "Add a new Course",
                Template.COURSES,
                List.of(
                        new FormText("Course Name", "name"),
                        new FormText("Course Acronym", "acr"),
                        new FormText("Coordinator Teacher Number", "teacher")));
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Form of " + Template.COURSES;
    }
}
