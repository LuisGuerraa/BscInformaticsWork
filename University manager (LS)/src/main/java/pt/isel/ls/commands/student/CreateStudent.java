package pt.isel.ls.commands.student;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class CreateStudent extends CommandHandler {

    private final TransactionManager tm;
    // USERS
    private String name = "name";
    private String email = "email";
    // STUDENTS
    private String num = "num";
    private String pid = "pid";

    public CreateStudent(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String userQuery = "INSERT INTO users VALUES (?,?)";
        String studQuery = "INSERT INTO student VALUES (?,?,?)";
        String studEmail = parameter.getValue(email);
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn()
                        .prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, studEmail);
                ps.setString(2, parameter.getValue(name));
                ps.executeUpdate();
                int studentNumber = Integer.parseInt(parameter.getValue(num));
                ps = k.getConn()
                        .prepareStatement(studQuery, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, studentNumber);
                ps.setString(2, studEmail);
                ps.setString(3, parameter.getValue(pid));

                ps.executeUpdate();
                k.commitTransaction();
                return new EmptyResultView()
                        .addHeader("Location", Template.getStudentOfNum(studentNumber));
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location", Template.NEW_FORM_STUDENTS + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{email, name, num, pid};
    }

    @Override
    public String getDescription() {
        return "Creates a new student, given the  parameters(num - student number, "
                + "name - student name, email - student email, pid - programme acronym).";
    }

}
