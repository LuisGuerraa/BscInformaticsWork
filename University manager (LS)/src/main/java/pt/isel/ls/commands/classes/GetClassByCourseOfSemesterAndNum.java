package pt.isel.ls.commands.classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Group;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;


public class GetClassByCourseOfSemesterAndNum extends CommandHandler {
    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num";
    private final TransactionManager tm;

    public GetClassByCourseOfSemesterAndNum(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String semester = parameter.getValue(sem);
        String acrCourse = parameter.getValue(acr);
        String classId = parameter.getValue(num);
        String classInfo = classId + "-" + acrCourse + "-" + semester;
        String title = "Class details of " + classId + " of " + acrCourse + " in " + semester;
        List<PathLink> list = List.of(
                new PathLink("Students of " + classInfo,
                        Template.getStudentClass(acrCourse, semester, classId)),
                new PathLink("Teachers of " + classInfo,
                        Template.getTeacherClass(acrCourse, semester, classId)),
                new PathLink("Groups of " + classInfo,
                        Template.getGroupsClass(acrCourse,semester,classId)),
                new PathLink(acrCourse + "-" + semester + " Classes Info",
                        "/courses/" + acrCourse + "/classes/" + semester)
        );

        String groupQuery = "SELECT * FROM groups "
                + "WHERE classID = ? AND yearSem = ? AND season = ? AND acrCourse = ?";

        int year = getYearOfSemester(semester);
        String season = getSeasonOfSemester(semester);
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(groupQuery);
                ps.setString(1, classId);
                ps.setInt(2, year);
                ps.setString(3, season);
                ps.setString(4, acrCourse);
                final List<Group> groups = EntitySet.getGroups(ps);

                k.commitTransaction();

                ComposedResult groupCr = new ComposedResult(
                        "List of Groups",
                        groups,
                        List.of("Group Number", "Class ID",
                                "Semester Year", "Season", "Course Acronym"),
                        new Property<>(
                                g -> String.valueOf(g.getGroupNumber()),
                                g -> Template.getGroupsGno(acrCourse,
                                        semester, classId, g.getGroupNumber())),
                        new Property<>(Group::getClassNumber),
                        new Property<>(g -> String.valueOf(g.getYear())),
                        new Property<>(Group::getSeason),
                        new Property<>(Group::getAcrCourse)
                );
                return new CommonResultView(title, list, groupCr);
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
        return "Shows the classes of the acr course on the sem semester and with num number.";
    }
}
