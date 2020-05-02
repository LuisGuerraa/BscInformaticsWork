package pt.isel.ls.commands.student;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;

public class AddStudentToClass extends CommandHandler {

    private final String acr = "acr";
    private final String sem = "sem";
    private final String num = "num"; // Class ID
    private final String numStu = "numStu";
    private final TransactionManager tm;

    public AddStudentToClass(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "INSERT INTO student_class VALUES (?,?,?,?,?)";
        String semester = parameter.getValue(sem);
        String classNum = parameter.getValue(num);
        String acronym = parameter.getValue(acr);
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(query);
                ps.setInt(1, Integer.parseInt(parameter.getValue(numStu)));
                ps.setString(2, classNum);
                ps.setInt(3, getYearOfSemester(semester));
                ps.setString(4, getSeasonOfSemester(semester));
                ps.setString(5, acronym);
                ps.executeUpdate();
                k.commitTransaction();
                return new EmptyResultView().addHeader("Location",
                        Template.getStudentClass(acronym, semester, classNum));
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location",
                                Template
                                        .newForm(Template.getStudentClass(acronym,
                                                semester, classNum)) + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{numStu, num, sem, acr};
    }

    @Override
    public String getDescription() {
        return "Adds a new student to a class, given the parameter"
                + "(numStu - student number).";
    }

}
