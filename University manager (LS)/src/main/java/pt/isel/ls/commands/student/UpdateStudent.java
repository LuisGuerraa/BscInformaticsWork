package pt.isel.ls.commands.student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class UpdateStudent extends CommandHandler {

    private final String num = "num";
    private final String name = "name";
    private final String email = "email";
    private final String pid = "pid";
    private final TransactionManager tm;

    public UpdateStudent(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String studentQuery = "UPDATE student SET num = ?, p_acr = ? WHERE num = ?";
        String userQuery = "UPDATE users SET email = ?, username = ? WHERE email = ?";
        String getEmailQuery = "SELECT U.email FROM users as U"
                + " INNER JOIN student AS S"
                + " ON u.email = s.email WHERE num = ? ";
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn().prepareStatement(studentQuery);
                List<String> numbers = parameter.getValues(num);
                if (numbers.size() != 2) {
                    throw new SQLException("Expected 2 separate student numbers,"
                            + " Found: " + numbers.size());
                }
                int actualNumber = Integer.parseInt(numbers.get(0));
                int formerNumber = Integer.parseInt(numbers.get(1));
                ps.setInt(1, actualNumber);
                ps.setString(2, parameter.getValue(pid));
                ps.setInt(3, formerNumber);
                ps.executeUpdate();

                //USER
                ps = k.getConn().prepareStatement(getEmailQuery);
                ps.setInt(1, actualNumber);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Email from user not found!");
                }
                String email = rs.getString(1);
                ps = k.getConn().prepareStatement(userQuery);
                ps.setString(1, parameter.getValue(this.email));
                ps.setString(2, parameter.getValue(name));
                ps.setString(3, email);
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
        return new String[]{num, email, name, pid};
    }

    @Override
    public String getDescription() {
        return "Updates an existent student, given all required parameters.";
    }

}
