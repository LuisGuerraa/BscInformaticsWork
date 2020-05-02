package pt.isel.ls;

import java.sql.SQLException;
import java.util.function.Function;

import pt.isel.ls.configuration.Connector;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;

public class TransactionManager {
    private final Connector connector;

    public TransactionManager(Connector connector) {
        this.connector = connector;
    }

    public ResultViewer run(Function<Connector, ResultViewer> function)
            throws SQLException {
        try {
            connector.openConnection();
            connector.startTransaction();
            return function.apply(connector);
        } catch (SQLException e) {
            UserInterface.info(e.getMessage(),
                    "Unable to acquire connection from Database! Please try again later!");
            return new EmptyResultView();
        } finally {
            connector.closeConnection(); // closeConnection() throws SQLException
        }
    }
}
