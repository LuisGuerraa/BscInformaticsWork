package pt.isel.ls.commands;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.UserInterface;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class GetTime extends CommandHandler {

    @Override
    public ResultViewer execute(Parameter parameter) throws SQLException {
        UserInterface.msg("Current time : " + new Timestamp(new Date().getTime()));
        return new EmptyResultView();
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Presents the current time.";
    }

}