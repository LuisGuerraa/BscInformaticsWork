package pt.isel.ls.commands.group;

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


public class GetGroupsOfClass extends CommandHandler {
    private final String num = "num";
    private final String sem = "sem";
    private final String acr = "acr";
    private final TransactionManager tm;

    public GetGroupsOfClass(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String semester = parameter.getValue(sem);
        String classNum = parameter.getValue(num);
        String acrCourse = parameter.getValue(acr);
        String classInfo = classNum + "-" + acrCourse + "-" + semester;
        String title = "List of groups of " + acrCourse + " in " + semester;
        String query = "SELECT * FROM groups "
                + "WHERE classID = ? AND season = ? AND yearSem = ? AND acrCourse = ?";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, classNum);
                ps.setString(2, getSeasonOfSemester(semester));
                ps.setInt(3, getYearOfSemester(semester));
                ps.setString(4, acrCourse);
                List<Group> groups = EntitySet.getGroups(ps);
                k.commitTransaction();
                return new CommonResultView(
                        title,
                        List.of(
                                new PathLink(classInfo + " Class Info",
                                        Template.getCoursesAcrClassesSemNum(
                                                acrCourse, semester, classNum)),
                                new PathLink("Form to add a Group to " + classInfo,
                                        Template.newForm(
                                                Template.getGroupsClass(
                                                        acrCourse,semester, classNum)))),
                        new ComposedResult(
                                title,
                                groups,
                                List.of("Group Number", "Class ID",
                                        "Semester Year", "Season", "Course Acronym"),
                                new Property<>(g -> String.valueOf(g.getGroupNumber()),
                                        g -> Template.getGroupsGno(
                                                g.getAcrCourse(), g.getYear() + g.getSeason(),
                                                g.getClassNumber(), g.getGroupNumber()
                                        )),
                                new Property<>(Group::getClassNumber),
                                new Property<>(g -> String.valueOf(g.getYear())),
                                new Property<>(Group::getSeason),
                                new Property<>(Group::getAcrCourse)
                                ));
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{num, sem, acr};
    }

    @Override
    public String getDescription() {
        return "Shows all groups of a class.";
    }

}
