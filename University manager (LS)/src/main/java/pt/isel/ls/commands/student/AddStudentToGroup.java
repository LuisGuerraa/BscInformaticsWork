package pt.isel.ls.commands.student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;

public class AddStudentToGroup extends CommandHandler {

    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num";
    private final String gno = "gno";
    private final String numStu = "numStu";   //num Student
    private final TransactionManager tm;

    public AddStudentToGroup(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String checkIfEnrolled = "SELECT * FROM student_class "
                + "WHERE numStudent = ? AND idClass = ? "
                + " AND yearSem = ? AND season = ? AND acrCourse = ? ";
        String semester = parameter.getValue(sem);
        int year = getYearOfSemester(semester);
        String season = getSeasonOfSemester(semester);
        int groupNumber = Integer.parseInt(parameter.getValue(gno));
        String classNum = parameter.getValue(num);
        String acronym = parameter.getValue(acr);

        return tm.run(k -> {
            try {
                for (String stNum : parameter.getValues(numStu)) {
                    int studentNumber = Integer.parseInt(stNum);
                    PreparedStatement ps = k.getConn().prepareStatement(checkIfEnrolled);
                    ps.setInt(1, studentNumber);
                    ps.setString(2, classNum);
                    ps.setInt(3, year);
                    ps.setString(4, season);
                    ps.setString(5, acronym);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new SQLException("Student of number "
                                + studentNumber + " is not enrolled in the stated class!");
                    }
                    String addStudentToGroup = "INSERT INTO group_members VALUES (?,?,?,?,?,?)";
                    ps = k.getConn().prepareStatement(addStudentToGroup);
                    ps.setInt(1, groupNumber);
                    ps.setInt(2, studentNumber);
                    ps.setString(3, classNum);
                    ps.setInt(4, year);
                    ps.setString(5, season);
                    ps.setString(6, acronym);
                    ps.executeUpdate();
                }
                k.commitTransaction();
                return new EmptyResultView()
                        .addHeader("Location",
                                Template.getGroupsGno(acronym,semester,classNum,groupNumber));
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location",
                                Template.newForm(
                                        Template.getGroupsGno(
                                        acronym,semester,classNum,groupNumber)) + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{numStu, num, sem, acr, gno};
    }

    @Override
    public String getDescription() {
        return "Adds a new student to a group, given the parameter"
                + "(numStu - student number) that can occur multiple times.";
    }

}
