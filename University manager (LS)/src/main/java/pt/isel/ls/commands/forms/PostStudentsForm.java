package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;

import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;

public class PostStudentsForm extends CommandHandler {

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        return new FormView(
                "Add a new Student",
                Template.STUDENTS,
                List.of(
                        new FormText("Student Number", "num"),
                        new FormText("Student Name", "name"),
                        new FormText("Student Email", "email"),
                        new FormText("Programme Acronym", "pid")));
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "";
    }
}
