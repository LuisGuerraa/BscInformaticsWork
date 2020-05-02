package pt.isel.ls.commands.teacher;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Course;
import pt.isel.ls.model.entities.Teacher;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.result.SingleResult;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;


public class GetTeacherOfNum extends CommandHandler {

    private final String num = "num";
    private final TransactionManager tm;

    public GetTeacherOfNum(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String teacherNum = parameter.getValue(num);
        String title = "Details of Teacher " + teacherNum;
        /*
        String query = "SELECT U.* FROM teacher AS T "
                + "INNER JOIN users AS U ON T.email = U.email WHERE T.num = ?";
        */
        String query = "SELECT * FROM teacher WHERE num = ?";
        String courseQuery = "SELECT DISTINCT C.* FROM course AS C "
                + "INNER JOIN teacher_class as TC "
                + "ON TC.acrCourse = C.acr "
                + "WHERE TC.numTeacher = ?";

        List<PathLink> linkList = List.of(
                new PathLink("Classes of Teacher  "
                        + teacherNum,"/teachers/" + teacherNum + "/classes"),
                new PathLink("Teachers", Template.TEACHERS));
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setInt(1, Integer.parseInt(teacherNum));
                final List<Teacher> teacherAttrs = EntitySet.getTeachers(ps);


                ps = k.getConn().prepareStatement(courseQuery);
                ps.setInt(1, Integer.parseInt(teacherNum));
                final List<Course> courses = EntitySet.getCourses(ps);

                k.commitTransaction();

                SingleResult teacherSr = new SingleResult(
                        title,
                        teacherAttrs,
                        List.of("Teacher Number", "Teacher Email"),
                        new Property<>(t -> String.valueOf(t.getNum())),
                        new Property<>(Teacher::getEmail)
                );
                ComposedResult courseCr = new ComposedResult(
                        "List of Courses",
                        courses,
                        List.of("Course Acronym", "Course Name", "Coordinator Teacher"),
                        new Property<>(
                                Course::getAcr,
                                c -> Template.getCoursesAcr(c.getAcr())),
                        new Property<>(Course::getName),
                        new Property<>(c -> String.valueOf(c.getNum()))
                );
                return new CommonResultView(title, linkList, List.of(teacherSr,courseCr));
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{num};
    }

    @Override
    public String getDescription() {
        return "Shows the teacher with number num.";
    }

}
