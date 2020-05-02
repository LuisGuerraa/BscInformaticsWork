package pt.isel.ls.commands.classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Class;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;


public class GetClassesOfCourse extends CommandHandler {
    private final String acr = "acr";
    private final TransactionManager tm;

    public GetClassesOfCourse(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acrCourse = parameter.getValue(acr);
        String title = "List of classes of " + acrCourse;
        String query = "SELECT * FROM class WHERE acrCourse = ?";
        List<PathLink> list = List.of(
                new PathLink(acrCourse + " Info", Template.getCoursesAcr(acrCourse)),
                new PathLink("Form to add a new Class to " + acrCourse,
                        Template.newForm(Template.getCoursesAcrClasses(acrCourse))));
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, acrCourse);
                List<Class> classes = EntitySet.getClasses(ps);
                k.commitTransaction();
                return new CommonResultView(
                        title,
                        list,
                        new ComposedResult(
                                title,
                                classes,
                                List.of("Class ID", "Semester Year",
                                        "Season", "Course Acronym", "Class Semester"),
                                new Property<>(c -> String.valueOf(c.getId())),
                                new Property<>(c -> String.valueOf(c.getYearSem())),
                                new Property<>(Class::getSeason),
                                new Property<>(Class::getAcrCourse),
                                new Property<>(c -> c.getYearSem() + c.getSeason(),
                                        c -> Template.getCoursesAcrClassesSem(c.getAcrCourse(),
                                                c.getYearSem() + c.getSeason()))
                                )
                );
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
        return "Shows all classes for a course.";
    }

}
