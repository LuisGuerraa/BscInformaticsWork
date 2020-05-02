package pt.isel.ls.commands.user;

import java.sql.SQLException;
import java.util.List;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.UserInterface;
import pt.isel.ls.model.EntitySet;
import pt.isel.ls.model.entities.User;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.ComposedResult;
import pt.isel.ls.result.Property;
import pt.isel.ls.resultview.CommonResultView;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class GetUsers extends CommandHandler {

    private final TransactionManager tm;

    public GetUsers(TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        String title = "List of Users";
        String query = "SELECT * FROM users";
        return tm.run(k -> {
            try {
                List<User> users = EntitySet.getUsers(k.getConn().prepareStatement(query));
                k.commitTransaction();
                return new CommonResultView(title,
                        new ComposedResult(
                                title,
                                users,
                                List.of("User Email", "Username"),
                                new Property<>(User::getEmail),
                                new Property<>(User::getUsername)
                                ));
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
        return "Shows all users.";
    }

}
