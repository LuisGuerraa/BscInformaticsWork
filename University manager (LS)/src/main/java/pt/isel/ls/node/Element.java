package pt.isel.ls.node;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Element implements Node {
    private String key;
    private final List<Node> children;
    private final Map<String, String> map;

    public Element(String key) {
        this.key = key;
        children = new LinkedList<>();
        map = new HashMap<>();
    }

    @Override
    public void writeTo(PrintStream printStream) {
        printStream.print("<");
        printStream.print(key);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            printStream.print(String.format(" %s=%s", entry.getKey(), entry.getValue()));
        }
        printStream.print(">");
        for (Node n : children) {
            n.writeTo(printStream);
        }
        printStream.print("</");
        printStream.print(key);
        printStream.print(">");
    }

    public Element addElem(String key) {
        Element element = new Element(key);
        children.add(element);
        return element;
    }

    public Element addAttributes(String name, String value) {
        map.put(name, value);
        return this;
    }

    public Node addNode(Node node) {
        children.add(node);
        return node;
    }

    public void addLink(String href, String txt) {
        addElem("a").addAttributes("href", href).addText(txt);
    }

    public Element addInput(String type, String name) {
        return addElem("input")
                .addAttributes("type",type)
                .addAttributes("name",name)
                .addAttributes("required", "");
    }

    public Element addInputWithValue(String type, String value) {
        return addElem("input")
                .addAttributes("type", type)
                .addAttributes("value", value);
    }

    public void addBr() {
        addText("<br>");
    }

    public Element addForm(String method, String actionUrl) {
        return addElem("form")
                .addAttributes("action", actionUrl)
                .addAttributes("method", method);
    }

    public Element addText(String element, String txt) {
        Element elem = new Element(element);
        Text text = new Text(txt);
        elem.addNode(text);
        this.addNode(elem);
        return elem;
    }

    public void addText(String txt) {
        Text text = new Text(txt);
        children.add(text);
    }

    public void addLabel(String description, String to) {
        addElem("label").addAttributes("for", to).addText(description);
    }
}
