package pt.isel.ls.commands;

import java.sql.SQLException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pt.isel.ls.App;
import pt.isel.ls.CommandHandler;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.response.ResponseServlet;
import pt.isel.ls.resultview.EmptyResultView;

import pt.isel.ls.resultview.ResultViewer;


public class StartServerListening extends CommandHandler {
    
    private final String port = "port";
    private ResponseServlet responseServlet;
    private Server server;
    private ServletHandler handler;

    public StartServerListening(App app) {
        responseServlet = new ResponseServlet(app);
    }

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        server = new Server(Integer.parseInt(parameter.getValue(port)));
        handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(responseServlet), "/*");
        try {
            server.start();
            UserInterface.msg("Server started");
        } catch (Exception e) {
            UserInterface.info(e.getMessage(), "Unable to start Server. Try again!");
        }
        return new EmptyResultView();
    }

    @Override
    public String[] getArguments() {
        return new String[]{port};
    }

    @Override
    public String getDescription() {
        return "Enables the HTTP interface.";
    }

}
