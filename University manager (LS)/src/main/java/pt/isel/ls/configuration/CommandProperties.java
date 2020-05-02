package pt.isel.ls.configuration;

import pt.isel.ls.CommandHandler;
import pt.isel.ls.request.Method;

public class CommandProperties {
    private Method method;
    private String template;
    private CommandHandler command;

    CommandProperties(Method method, String template, CommandHandler command) {
        this.method = method;
        this.template = template;
        this.command = command;
    }

    public Method getMethod() {
        return method;
    }

    public String getTemplate() {
        return template;
    }

    public CommandHandler getCommand() {
        return command;
    }
}
