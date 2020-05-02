package pt.isel.ls.commands.course;

import java.sql.SQLException;
import java.util.Collections;
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


public class GetCourses extends CommandHandler {
    private final TransactionManager tm;

    public GetCourses(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "SELECT * FROM course";
        String title = "List Of Courses";
        return tm.run(k -> {
            try {
                List<Course> courses = EntitySet.getCourses(k.getConn().prepareStatement(query));
                k.commitTransaction();
                return new CommonResultView(
                        title,
                        Collections.singletonList(
                                new PathLink("Form to create a new Course",
                                        Template.NEW_FORM_COURSES)),
                        new ComposedResult(
                                title,
                                courses,
                                List.of("Course Acronym", "Course Name", "Coordinator Teacher"),
                                new Property<>(Course::getAcr,
                                        c -> Template.getCoursesAcr(c.getAcr())),
                                new Property<>(Course::getName),
                                new Property<>(c -> String.valueOf(c.getNum())))
                );
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
        return "Shows all courses.";
    }

}
