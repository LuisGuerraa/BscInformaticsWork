package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.configuration.Configuration;
import pt.isel.ls.configuration.Connector;

public class Main {

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
        app.run(args);
    }
}
