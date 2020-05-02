package pt.isel.ls.commands.teacher;

import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Teacher;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;


public class GetTeachers extends CommandHandler {

    private final TransactionManager tm;

    public GetTeachers(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String title = "List Of Teachers";
        String query = "SELECT * FROM Teacher";
        return tm.run(k -> {
            try {
                List<Teacher> teachers = EntitySet.getTeachers(
                        k.getConn().prepareStatement(query)
                );
                k.commitTransaction();
                return new CommonResultView(title,
                        List.of(new PathLink("Form to add a new Teacher",
                                Template.NEW_FORM_TEACHERS)),
                        new ComposedResult(
                                title,
                                teachers,
                                List.of("Teacher Number", "Teacher Email"),
                                new Property<>(t -> String.valueOf(t.getNum()),
                                        t -> Template.getTeacherOfNum(t.getNum())),
                                new Property<>(Teacher::getEmail)
                                ));
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Shows all teachers.";
    }

}

