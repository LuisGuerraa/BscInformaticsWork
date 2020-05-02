package pt.isel.ls.commands.student;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.Class;
import pt.isel.ls.model.entities.Group;
import pt.isel.ls.model.entities.Student;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.result.SingleResult;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.PathLink;
import pt.isel.ls.resultview.ResultViewer;

public class GetStudentOfNum extends CommandHandler {
    private final String num = "num";
    private final TransactionManager tm;

    public GetStudentOfNum(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        int studentNum = Integer.parseInt(parameter.getValue(num));
        String title = "Details of Student " + studentNum;
        String classQuery = "SELECT idClass,yearSem,season,acrCourse "
                + "FROM student_class WHERE numStudent=?";
        String groupQuery = "SELECT number,classID,yearSem,season,acrCourse "
                + "FROM group_members WHERE studentNumber=?";
        String studentQuery = "SELECT * FROM Student WHERE num = ?";
        return tm.run(k -> {
            try {
                PreparedStatement getStudent = k.getConn().prepareStatement(studentQuery);
                getStudent.setInt(1, studentNum);
                final List<Student> students = EntitySet.getStudents(getStudent);

                PreparedStatement getClasses = k.getConn().prepareStatement(classQuery);
                getClasses.setInt(1, studentNum);
                final List<Class> classes = EntitySet.getClasses(getClasses);

                PreparedStatement getGroups = k.getConn().prepareStatement(groupQuery);
                getGroups.setInt(1, studentNum);
                final List<Group> groups = EntitySet.getGroups(getGroups);

                k.commitTransaction();
                SingleResult studentResult = new SingleResult(
                        title,
                        students,
                        List.of("Student Number", "Student Email", "Student Programme"),
                        new Property<>(s -> String.valueOf(s.getNumber())),
                        new Property<>(Student::getEmail),
                        new Property<>(
                                Student::getProgramme,
                                s -> Template.getProgrammesPid(s.getProgramme()))
                );
                ComposedResult listOfClasses = new ComposedResult(
                        "List of Classes",
                        classes,
                        List.of("Class ID", "Semester Year", "Season",
                                "Course Acronym", "Class ID"),
                        new Property<>(c -> String.valueOf(c.getId())),
                        new Property<>(c -> String.valueOf(c.getYearSem())),
                        new Property<>(Class::getSeason),
                        new Property<>(Class::getAcrCourse),
                        new Property<>(c -> c.getId() + "-" + c.getYearSem() + c.getSeason(),
                                c -> Template.getCoursesAcrClassesSemNum(
                                        c.getAcrCourse(), c.getYearSem()
                                                + c.getSeason(), c.getId()))
                );
                ComposedResult listOfGroups = new ComposedResult(
                        "List of Groups",
                        groups,
                        List.of("Group Number", "Class ID", "Semester Year",
                                "Season", "Course Acronym"),
                        new Property<>(
                                g -> String.valueOf(g.getGroupNumber()),
                                g -> Template.getGroupsGno(
                                        g.getAcrCourse(),
                                        g.getYear() + g.getSeason(),
                                        g.getClassNumber(),
                                        g.getGroupNumber())),
                        new Property<>(Group::getClassNumber),
                        new Property<>(g -> String.valueOf(g.getYear())),
                        new Property<>(Group::getSeason),
                        new Property<>(Group::getAcrCourse));
                return new CommonResultView(
                                title,
                        List.of(
                                new PathLink("Students",Template.STUDENTS)),
                        List.of(
                                studentResult,
                                listOfClasses,
                                listOfGroups)
                );
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
        return "Shows the student with the number num.";
    }

}
