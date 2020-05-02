package pt.isel.ls.commands.course;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Course;
import pt.isel.ls.model.entities.Programme;
import pt.isel.ls.model.entities.Teacher;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;


public class GetCourseOfAcr extends CommandHandler {
    private final String acr = "acr";
    private final TransactionManager tm;

    public GetCourseOfAcr(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acrCourse = parameter.getValue(acr);
        List<PathLink> linksList = List.of(new PathLink("Courses", Template.COURSES),
                new PathLink("Classes of " + acrCourse
                        + " Course", "/courses/" + acrCourse + "/classes"));
        String title = "Details of " + acrCourse;

        String teachersQuery = "SELECT DISTINCT T.num, T.email FROM teacher_class as TC "
                + "INNER JOIN teacher as T "
                + "ON T.num = TC.numTeacher "
                + "WHERE TC.acrCourse = ?";
        String progQuery = "SELECT DISTINCT P.* FROM ("
                + "SELECT  * FROM mandatory_course as MC "
                + "UNION "
                + "SELECT * FROM optional_course as OC "
                + ") as C "
                + "INNER JOIN programme as P "
                + "ON C.acrProgramme = P.acr "
                + "WHERE C.acr = ?";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(teachersQuery);
                ps.setString(1, acrCourse);
                final List<Teacher> teachers = EntitySet.getTeachers(ps);

                ps = k.getConn().prepareStatement(progQuery);
                ps.setString(1, acrCourse);
                final List<Programme> programmes = EntitySet.getProgrammes(ps);

                k.commitTransaction();

                ComposedResult teacherCr = new ComposedResult(
                        "List of Teachers",
                        teachers,
                        List.of("Teacher Number", "Teacher Email"),
                        new Property<>(t -> String.valueOf(t.getNum()),
                                t -> Template.getTeacherOfNum(t.getNum())),
                        new Property<>(Teacher::getEmail));

                ComposedResult progCr = new ComposedResult(
                        "List of Programmes",
                        programmes,
                        List.of("Programme Acronym", "Programme Name", "Programme Length"),
                        new Property<>(Programme::getAcr,
                                p -> Template.getProgrammesPid(p.getAcr())),
                        new Property<>(Programme::getName),
                        new Property<>(p -> String.valueOf(p.getLength()))
                );
                return new CommonResultView(title, linksList, List.of(teacherCr, progCr));
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });

    }

    @Override
    public String[] getArguments() {
        return new String[]{acr};
    }

    @Override
    public String getDescription() {
        return "Shows the course with the acr acronym.";
    }

}
