package pt.isel.ls.commands.group;

import java.sql.PreparedStatement;
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


public class GetGroupOfNum extends CommandHandler {
    private final String num = "num";
    private final String sem = "sem";
    private final String acr = "acr";
    private final String gno = "gno";
    private final TransactionManager tm;

    public GetGroupOfNum(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        int groupNum = Integer.parseInt(parameter.getValue(gno));
        String semester = parameter.getValue(sem);
        String classNumber = parameter.getValue(num);
        String classDetail = classNumber + "-" + semester;
        String acrCourse = parameter.getValue(acr);
        String title = "Class " + classDetail + ": Group " + groupNum;
        String h2 = title + " - List of Students";
        String query = "SELECT S.* FROM student AS S "
                + "INNER JOIN group_members AS GM "
                + "on S.num = GM.studentNumber "
                + "WHERE GM.number = ? AND classID = ? "
                + "AND yearSem = ? AND season = ? AND acrCourse = ?";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setInt(1, groupNum);
                ps.setString(2, classNumber);
                ps.setInt(3, getYearOfSemester(semester));
                ps.setString(4, getSeasonOfSemester(semester));
                ps.setString(5, acrCourse);

                List<Student> students = EntitySet.getStudents(ps);
                k.commitTransaction();
                String groupInfo = "G" + groupNum + "-"
                        + classNumber + "-" + acrCourse + "-" + semester;
                return new CommonResultView(title,
                        List.of(new PathLink(acrCourse + "-" + classDetail,
                                Template.getCoursesAcrClassesSemNum(acrCourse,
                                        semester, classNumber)),
                                new PathLink("Form to add a Student to " + groupInfo,
                                        Template.newForm(
                                                Template.getGroupsGno(
                                                        acrCourse,semester,classNumber,groupNum)
                                        ))),
                        new ComposedResult(
                                h2,
                                students,
                                List.of("Student Number", "Student Email", "Student Programme"),
                                new Property<>(
                                        s -> String.valueOf(s.getNumber()),
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
        return new String[]{num, sem, acr, gno};
    }

    @Override
    public String getDescription() {
        return "Shows the details for the group with the indicated number.";
    }

}
