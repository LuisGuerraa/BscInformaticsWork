package pt.isel.ls.commands.student;

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


public class GetStudentsOfClass extends CommandHandler {
    private final String num = "num";
    private final String sem = "sem";
    private final String acr = "acr";
    private final TransactionManager tm;

    public GetStudentsOfClass(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acrCourse = parameter.getValue(acr);
        String semester = parameter.getValue(sem);
        String classId = parameter.getValue(num);
        String classNum = classId + "-" + acrCourse + "-" + semester;

        String h2 = "List of Students in " + classNum;
        String title = classNum + " - students";
        String query = "SELECT * FROM student as ST "
                + "INNER JOIN student_class as SC "
                + "ON ST.num = SC.numStudent "
                + "WHERE idClass = ? AND season = ? AND yearSem = ? AND acrCourse = ?";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, classId);
                ps.setString(2, getSeasonOfSemester(semester));
                ps.setInt(3, getYearOfSemester(semester));
                ps.setString(4, acrCourse);
                List<Student> students = EntitySet.getStudents(ps);
                k.commitTransaction();
                return new CommonResultView(title,
                        List.of(
                                new PathLink(classNum + " Classes",
                                        Template
                                                .getCoursesAcrClassesSemNum(acrCourse,
                                                        semester,classId)),
                                new PathLink("Form to add a Student to " + classNum,
                                        Template
                                                .newForm(Template.getStudentClass(acrCourse,
                                                        semester, classId)))),
                        new ComposedResult(
                                h2,
                                students,
                                List.of("Student Number", "Student Email", "Student Programme"),
                                new Property<>(
                                        s -> String.valueOf(s.getNumber()),
                                        s -> Template.getStudentOfNum(s.getNumber())),
                                new Property<>(Student::getEmail),
                                new Property<>(Student::getProgramme))
                                );
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
        return "Shows all students of a class.";
    }

}
