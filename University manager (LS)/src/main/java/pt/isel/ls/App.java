package pt.isel.ls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Scanner;

import pt.isel.ls.configuration.CommandProperties;
import pt.isel.ls.request.Header;
import pt.isel.ls.request.Parameter;
import pt.isel.ls.request.Request;
import pt.isel.ls.resultview.ResultViewer;

public class App {
    private static final int minsize = 2;
    private static final int maxsize = 4;
    private final PrintStream defaultPrintStream;
    private static final String wrongRequest = "%s %s : %s";
    private final Router router;

    App(Router router) {
        this.router = router;
        defaultPrintStream = System.out;
    }

    void build(CommandProperties[] commandsProperties) {
        for (CommandProperties property : commandsProperties) {
            router.add(property.getMethod(), property.getTemplate(), property.getCommand());
        }
    }

    public void run(String[] args) {
        if (args.length == 0) {
            interactiveMode();
        } else if (checkArgumentsSize(args)) {
            executeCommand(args);
        } else {
            UserInterface.msg("Error: Incorrect number of arguments. Found : "
                    + args.length);
        }
    }

    private boolean checkArgumentsSize(String[] args) {
        return args.length >= minsize && args.length <= maxsize;
    }

    public Request getRequest(String[] arguments) {
        String method = arguments[0];
        String path = arguments[1];
        String parameters = null;
        Header header = null;
        if (arguments.length >= 3) {
            header = getHeader(arguments[2]);
            if (header == null) {
                parameters = arguments[2];
            } else if (arguments.length == 4) {
                parameters = arguments[3];
            }
        }
        if (header == null) {
            header = new Header(defaultPrintStream, false);
        }
        return new Request(method, path, header, parameters);
    }

    private Header getHeader(String header) {
        String accept = "accept:text/";
        String fileName = "file-name:";
        String html = "html";
        String splitFormat = "\\|";

        if (!header.startsWith(accept)) {
            return null;
        }

        PrintStream ps = defaultPrintStream;
        boolean htmlFormat = false;

        String[] text = header.split(splitFormat);
        String firstWord = text[0];
        if (firstWord.substring(accept.length()).equals(html)) {
            htmlFormat = true;
        }
        if (text.length > 1) {
            String secondWord = text[1];
            if (secondWord.startsWith(fileName)) {
                try {
                    ps = new PrintStream(
                            new FileOutputStream(secondWord.substring(fileName.length()))
                    );
                } catch (FileNotFoundException e) {
                    UserInterface.info(e.getMessage(),
                            "Could not use desired file. Try again with a different file");
                    ps = defaultPrintStream;
                }
            }
        }
        return new Header(ps, htmlFormat);
    }

    public void executeCommand(String[] arguments) {
        try {
            Request request = getRequest(arguments);
            Header header = request.getHeader();
            ResultViewer result = executeRequest(request);
            if (result != null) {
                PrintStream ps = header.getPrintStream();
                if (header.isHtml()) {
                    result.printHtml(ps);
                } else {
                    result.printPlain(ps);
                }
                request.closePrintStream();
            }
        } catch (SQLException e) {
            UserInterface.msg(e.getMessage());
        } catch (IllegalArgumentException ex) {
            UserInterface.info(ex.getMessage(), "Error: Please insert a correct method.");
        }
    }

    public ResultViewer executeRequest(Request request) throws SQLException {
        Parameter parameter = request.getParameters();
        CommandHandler cm = router.get(request);
        if (cm == null) {
            UserInterface.msg(String.format(wrongRequest,
                            request.getMethod(),request.getPath(),
                            "Command not found. \n"));
        } else if (parameter.checkArguments(cm.getArguments())) {
            return cm.execute(request.getParameters());
        } else {
            UserInterface.msg("Inserted parameters don't match the requirements of the command!\n"
                    + "Description of command: " + cm.getDescription());
        }
        return null;
    }

    private void interactiveMode() {
        Scanner input = new Scanner(System.in);
        String line;
        UserInterface.msg("\nPlease insert commands by the following procedure: "
                + "{method} {path} {headers} {parameters}.\n");
        while (true) {
            line = input.nextLine();
            String[] split = line.split(" ");
            if (checkArgumentsSize(split)) {
                executeCommand(split);
            } else {
                UserInterface.msg("\nPlease insert valid arguments.");
                continue;
            }
            UserInterface.msg("\nReady to insert a new command:");
        }
    }

}