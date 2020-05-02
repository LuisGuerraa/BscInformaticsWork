package pt.isel.ls.commands.programme;

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


public class CreateProgramme extends CommandHandler {

    private final String pid = "pid";
    private final String name = "name";
    private final String length = "length";
    private final TransactionManager tm;

    public CreateProgramme(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String query = "INSERT INTO programme VALUES (?,?,?)";
        String programme = parameter.getValue(pid);
        return tm.run(k -> {
            try {
                PreparedStatement ps = k.getConn()
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, programme);
                ps.setString(2, parameter.getValue(name));
                ps.setInt(3, Integer.parseInt(parameter.getValue(length)));
                ps.executeUpdate();
                k.commitTransaction();
                return new EmptyResultView().addHeader("Location",
                        Template.getProgrammesPid(programme));
            } catch (SQLException | NumberFormatException e) {
                UserInterface.info(e.getMessage(), error);
                return new EmptyResultView()
                        .addHeader("Location", Template.NEW_FORM_PROGRAMMES + errorQuery);
            }
        });
    }

    @Override
    public String[] getArguments() {
        return new String[]{pid, name, length};
    }

    @Override
    public String getDescription() {
        return "Creates a new programme, given the parameters(pid - programme acronym, "
                + "name - programme name, length - number of semesters).";
    }

}
