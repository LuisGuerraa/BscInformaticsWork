package pt.isel.ls.commands;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;

public class ExitApp extends CommandHandler {

    @Override
    public ResultViewer execute(Parameter parameter) {
        System.exit(0);
        return new EmptyResultView();
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Terminates the application.";
    }
}
