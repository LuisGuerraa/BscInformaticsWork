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

public class GetClassesOfCourseAndSemester extends CommandHandler {
    private final String acr = "acr";
    private final String sem = "sem";
    private final TransactionManager tm;

    public GetClassesOfCourseAndSemester(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String semester = parameter.getValue(sem);
        String acrCourse = parameter.getValue(acr);
        String title = "List of classes of " + acrCourse + " in " + semester;
        String query = "SELECT * FROM class "
                + "WHERE acrCourse = ? and yearSem = ? AND season = ? ";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, acrCourse);
                ps.setInt(2, getYearOfSemester(semester));
                ps.setString(3, getSeasonOfSemester(semester));
                List<Class> classes = EntitySet.getClasses(ps);
                k.commitTransaction();
                return new CommonResultView(
                        title,
                        List.of(new PathLink(acrCourse
                               + " - Classes Info", Template.getCoursesAcrClasses(acrCourse))),
                        new ComposedResult(
                                title,
                                classes,
                                List.of("Class Number", "Semester Year",
                                        "Season", "Course Acronym", "Class ID"),
                                new Property<>(c -> String.valueOf(c.getId())),
                                new Property<>((c -> String.valueOf(c.getYearSem()))),
                                new Property<>(Class::getSeason),
                                new Property<>(Class::getAcrCourse),
                                new Property<>(c -> c.getId() + "-"
                                       + c.getYearSem() + c.getSeason(),
                                        c -> Template.getCoursesAcrClassesSemNum(
                                                c.getAcrCourse(), c.getYearSem()
                                                        + c.getSeason(), c.getId()))
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
        return new String[]{acr, sem};
    }

    @Override
    public String getDescription() {
        return "Shows all classes of the acr course on the sem semester.";
    }

}
