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
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;


public class GetCoursesOfProgrammePid extends CommandHandler {
    private final String pid = "pid";
    private final TransactionManager tm;

    public GetCoursesOfProgrammePid(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String progId = parameter.getValue(pid);
        String title = "List of Courses of " + progId;
        String query = "SELECT DISTINCT C.* FROM "
                + "(SELECT * FROM mandatory_course AS MC "
                + "UNION "
                + "SELECT * FROM optional_course AS OC "
                + ") AS MO_C "
                + "INNER JOIN COURSE AS C "
                + "ON MO_C.acr = C.acr "
                + "WHERE MO_C.acrProgramme = ?";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, progId);
                List<Course> courses = EntitySet.getCourses(ps);
                return new CommonResultView(
                        title,
                        List.of(
                                new PathLink(progId + " Info",
                                        Template.getProgrammesPid(progId)),
                                new PathLink("Form to add a Course to " + progId,
                                        Template.newForm(Template
                                                .getProgrammesPidCourses(progId)))),
                        new ComposedResult(
                                title,
                                courses,
                                List.of("Course Acronym", "Course Name", "Coordinator Teacher"),
                                new Property<>(Course::getAcr,
                                        c -> Template.getCoursesAcr(c.getAcr())),
                                new Property<>(Course::getName),
                                new Property<>(c -> String.valueOf(c.getNum()))));
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{pid};
    }

    @Override
    public String getDescription() {
        return "Shows the course structure of programme pid.";
    }

}
