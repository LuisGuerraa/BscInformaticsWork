package pt.isel.ls.commands;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.UserInterface;
import pt.isel.ls.configuration.CommandProperties;
import pt.isel.ls.configuration.Configuration;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.resultview.EmptyResultView;
import pt.isel.ls.resultview.ResultViewer;


public class GetCommands extends CommandHandler {
    private Configuration configuration;

    public GetCommands(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ResultViewer execute(Parameter parameter) {
        for (CommandProperties property : configuration.getCommandsProperties()) {
            UserInterface.msg(property.getMethod() + " " + property.getTemplate()
                    + " -> " + property.getCommand().getDescription() + "\n");
        }
        return new EmptyResultView();
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Presents a list of available commands and their description.";
    }

}
