package pt.isel.ls.commands.teacher;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class AddTeacherToClass extends CommandHandler {

    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num";
    private final String numDoc = "numDoc";   //num Teacher
    private final TransactionManager tm;

    public AddTeacherToClass(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "INSERT INTO teacher_class VALUES (?,?,?,?,?)";
        String semester = parameter.getValue(sem);
        String classNum = parameter.getValue(num);
        String acronym = parameter.getValue(acr);
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setString(1, classNum);
                ps.setInt(2, getYearOfSemester(semester));
                ps.setString(3, getSeasonOfSemester(semester));
                ps.setInt(4, Integer.parseInt(parameter.getValue(numDoc)));
                ps.setString(5, acronym);
                ps.executeUpdate();
                k.commitTransaction();
                return new EmptyResultView()
                        .addHeader("Location",
                                Template.getTeacherClass(acronym, semester, classNum));
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location",
                                Template.newForm(
                                        Template.getTeacherClass(
                                                acronym, semester, classNum)) + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{acr, sem, num, numDoc};
    }

    @Override
    public String getDescription() {
        return "Adds a new teacher to a class, given the "
                + "parameter(numDoc - teacher number).";
    }

}
