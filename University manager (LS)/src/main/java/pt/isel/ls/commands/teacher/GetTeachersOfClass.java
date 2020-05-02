package pt.isel.ls.commands.teacher;

import java.sql.PreparedStatement;
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


public class GetTeachersOfClass extends CommandHandler {

    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num";
    private final TransactionManager tm;

    public GetTeachersOfClass(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String semester = parameter.getValue(sem);
        String acrCourse = parameter.getValue(acr);
        String number = parameter.getValue(num);
        String classNum = number + "-" + semester;
        String h2 = "List of teachers in class " + classNum;
        String title = "Class " + classNum + " - teachers";
        String query = "SELECT T.* FROM teacher_class as TC "
                + "INNER JOIN teacher as T "
                + "ON T.num = TC.numTeacher "
                + "WHERE TC.acrCourse = ? AND TC.yearSem = ? AND TC.season = ? AND TC.id = ?";

        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, acrCourse);
                ps.setInt(2, getYearOfSemester(semester));
                ps.setString(3, getSeasonOfSemester(semester));
                ps.setString(4, number);
                List<Teacher> teachers = EntitySet.getTeachers(ps);
                k.commitTransaction();
                return new CommonResultView(title,
                        List.of(
                                new PathLink(acrCourse + "-" + classNum + " Classes",
                                        Template.getCoursesAcrClassesSemNum(acrCourse,
                                                semester, number)),
                                new PathLink("Form to add a Teacher to the Class",
                                        Template.newForm(Template.getTeacherClass(acrCourse,
                                                semester, number)))),
                        List.of(
                        new ComposedResult(
                                h2,
                                teachers,
                                List.of("Teacher Number", "Teacher Email"),
                                new Property<>(
                                        t -> String.valueOf(t.getNum()),
                                        t -> Template.getTeacherOfNum(t.getNum())),
                                new Property<>(Teacher::getEmail)))
                );
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{acr, sem, num};
    }

    @Override
    public String getDescription() {
        return "Shows all teachers for a class.";
    }

}
