package pt.isel.ls.commands.student;

import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Student;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;





public class GetStudents extends CommandHandler {

    private final TransactionManager tm;

    public GetStudents(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "SELECT * FROM Student";
        String title = "List Of Students";
        return tm.run(k -> {
            try {
                List<Student> students =
                        EntitySet.getStudents(k.getConn().prepareStatement(query));
                k.commitTransaction();
                return new CommonResultView(title,
                        List.of(new PathLink("Form to add a new Student",
                                Template.NEW_FORM_STUDENTS)),
                        new ComposedResult(
                        title,
                        students,
                        List.of("Student Number", "Student Email", "Student Programme"),
                        new Property<>(s -> String.valueOf(s.getNumber()),
                                s -> Template.getStudentOfNum(s.getNumber())),
                        new Property<>(Student::getEmail),
                        new Property<>(Student::getProgramme)));
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
        return "Shows all students.";
    }

}
