package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.configuration.Configuration;
import pt.isel.ls.configuration.Connector;

public class HerokuMain {

    private static final int LISTEN_PORT = 8080;

    public static void main(String[] args) {
        // Build Configs

        PGSimpleDataSource ds = new PGSimpleDataSource();
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
        if (jdbcUrl == null) {
            UserInterface.log("JDBC_DATABASE_URL is not defined, ending");
            return;
        }
        ds.setUrl(jdbcUrl);
        App app = new App(new Router());
        Configuration config = new Configuration(app,
                new TransactionManager(new Connector(ds))).initProperties();
        app.build(config.getCommandsProperties());
        String portDef = System.getenv("PORT");
        int port = portDef == null ? LISTEN_PORT : Integer.parseInt(portDef);
        String[] listen = {"listen", "/", "port=" + port};
        UserInterface.log("Listening on port " + port);
        app.run(listen);
    }
}

