package pt.isel.ls.request;

import java.io.PrintStream;

public class Header {
    private PrintStream printStream;
    private boolean isHtml;

    public Header(PrintStream printStream, boolean isHtml) {
        this.printStream = printStream;
        this.isHtml = isHtml;
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void close() {
        if (printStream != System.out) {
            printStream.close();
        }
    }
}