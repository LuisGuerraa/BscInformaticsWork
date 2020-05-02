package pt.isel.ls.commands.forms;

import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.ResultViewer;
import pt.isel.ls.resultview.form.FormText;
import pt.isel.ls.resultview.form.FormView;

public class PostProgrammeForm extends CommandHandler {

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        return new FormView(
                "Add a new Programme",
                Template.PROGRAMMES,
                List.of(
                        new FormText("Programme Acronym", "pid"),
                        new FormText("Programme Name", "name"),
                        new FormText("Number of Semesters", "length")));
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
