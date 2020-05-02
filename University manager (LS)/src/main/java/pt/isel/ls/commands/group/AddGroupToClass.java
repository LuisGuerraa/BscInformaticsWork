package pt.isel.ls.commands.group;

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


public class AddGroupToClass extends CommandHandler {
    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num";
    private final String numStu = "numStu";
    private final TransactionManager tm;

    public AddGroupToClass(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String highestGroup = "SELECT number FROM groups WHERE classID = ? AND yearSem = ? "
                + "AND season = ? AND acrCourse = ? ORDER BY number DESC";
        String groupQuery = "INSERT INTO groups VALUES (?,?,?,?,?)";
        String checkIfEnrolled = "SELECT * FROM student_class WHERE numStudent = ? AND idClass = ? "
                + " AND yearSem = ? AND season = ? AND acrCourse = ? ";
        String addStudentToGroup = "INSERT INTO group_members VALUES (?,?,?,?,?,?)";

        String classNum = parameter.getValue(num);
        String acronym = parameter.getValue(acr);
        String semester = parameter.getValue(sem);
        int year = getYearOfSemester(semester);
        String season = getSeasonOfSemester(semester);
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(highestGroup);
                ps.setString(1, classNum);
                ps.setInt(2, year);
                ps.setString(3, season);
                ps.setString(4, acronym);
                ResultSet rs = ps.executeQuery();

                int groupNumber = 1;
                if (rs.next()) {
                    groupNumber = rs.getInt(1) + 1;
                }
                //UserInterface.msg("Grupo nº -> " + groupNumber);

                ps = k.getConn().prepareStatement(groupQuery);
                ps.setInt(1, groupNumber);
                ps.setString(2, classNum);
                ps.setInt(3, year);
                ps.setString(4, season);
                ps.setString(5, acronym);
                ps.executeUpdate();

                for (String str : parameter.getValues(numStu)) {
                    int studentNumber = Integer.parseInt(str);
                    ps = k.getConn().prepareStatement(checkIfEnrolled);
                    ps.setInt(1, studentNumber);
                    ps.setString(2, classNum);
                    ps.setInt(3, year);
                    ps.setString(4, season);
                    ps.setString(5, acronym);
                    rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new SQLException("Student of number "
                                + str + " not enrolled in the stated class!");
                    }
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
                        .addHeader("Location", Template.newForm(
                                Template.getGroupsClass(
                                        acronym, semester, classNum) + errorQuery));
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{acr, sem, num, numStu};
    }

    @Override
    public String getDescription() {
        return "Adds a new group to a class, given the parameter"
                + "(numStu - student Number) that can occur multiple times.";
    }

}
