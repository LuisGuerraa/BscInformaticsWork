package pt.isel.ls.configuration;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class Connector {

    private Connection conn;
    private DataSource dataSource;

    public Connector(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void openConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = dataSource.getConnection();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void startTransaction() throws SQLException {
        conn.setAutoCommit(false);
    }

    private void rollbackTransaction() throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    public void commitTransaction() throws SQLException {
        conn.commit();
        conn.setAutoCommit(true);
    }

    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            if (!conn.getAutoCommit()) {
                rollbackTransaction();
            }
            conn.close();
        }
    }
}
