package pt.isel.ls.node;

import java.io.PrintStream;

public class Text implements Node {
    private String text;

    Text(String text) {
        this.text = text;
    }

    @Override
    public void writeTo(PrintStream printStream) {
        printStream.print(text);
    }
}
