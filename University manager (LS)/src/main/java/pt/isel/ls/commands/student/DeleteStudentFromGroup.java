package pt.isel.ls.commands.student;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class DeleteStudentFromGroup extends CommandHandler {

    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num";
    private final String gno = "gno";
    private final String numStu = "numStu";
    private final TransactionManager tm;

    public DeleteStudentFromGroup(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String acronym = parameter.getValue(acr);
        String semester = parameter.getValue(sem);
        String classNum = parameter.getValue(num);
        int groupNum = Integer.parseInt(parameter.getValue(gno));
        int studNum = Integer.parseInt(parameter.getValue(numStu));
        int year = getYearOfSemester(semester);
        String season = getSeasonOfSemester(semester);
        String query = "DELETE FROM group_members WHERE number = ? AND studentNumber = ? "
                + "AND classID = ? AND yearSem = ? AND season = ? AND acrCourse = ? ";

        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setInt(1, groupNum);
                ps.setInt(2, studNum);
                ps.setString(3, classNum);
                ps.setInt(4, year);
                ps.setString(5, season);
                ps.setString(6, acronym);
                ps.executeUpdate();
                k.commitTransaction();
                return new EmptyResultView();
            } catch (SQLException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView();
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Removes a student from a group.";
    }

}
