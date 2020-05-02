package pt.isel.ls.commands.teacher;

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


public class CreateTeacher extends CommandHandler {

    // USERS
    private final String name = "name";
    private final String email = "email";
    // TEACHER
    private final String num = "num";
    private final TransactionManager tm;

    public CreateTeacher(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query1 = "INSERT INTO users VALUES (?,?)";
        String query2 = "INSERT INTO teacher VALUES (?,?)";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn()
                        .prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
                String userEmail = parameter.getValue(email);
                ps.setString(1, userEmail);
                ps.setString(2, parameter.getValue(name));
                ps.executeUpdate();

                int teacherNum = Integer.parseInt(parameter.getValue(num));
                ps = k.getConn()
                        .prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, teacherNum);
                ps.setString(2, userEmail);

                ps.executeUpdate();
                k.commitTransaction();
                return new EmptyResultView()
                        .addHeader("Location", Template.getTeacherOfNum(teacherNum));
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location", Template.NEW_FORM_TEACHERS + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{email, name, num};
    }

    @Override
    public String getDescription() {
        return "Creates a new teacher, given the parameters(num - teacher number,"
                + " name - teacher name, email - teacher email).";
    }

}
