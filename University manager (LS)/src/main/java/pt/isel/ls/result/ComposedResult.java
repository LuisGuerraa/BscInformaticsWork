package pt.isel.ls.result;

import java.util.LinkedList;
import java.util.List;

import pt.isel.ls.model.Entity;
import pt.isel.ls.node.Element;

public class ComposedResult extends Result {
    private final List<String> headings;
    private final Element table;

    @SafeVarargs
    public <T extends Entity> ComposedResult(String titleOfResult,
                              List<T> list,
                              List<String> headings,
                              Property<T>... props) {
        super(titleOfResult, list);
        this.headings = headings;
        table = createTable(list, props).addAttributes("border", "1");
    }

    private <T> Element createTable(List<T> list, Property<T>[] props) {
        Element table = new Element("table");
        Element tableHead = table.addElem("tr");
        for (String label : headings) {
            tableHead.addText("th", label);
        }
        for (T obj : list) {
            Element tableData = table.addElem("tr");
            for (Property<T> prop : props) {
                Element td = tableData.addElem("td");
                String value = prop.getFunc().apply(obj);
                if (prop.hasLink()) {
                    td.addLink(prop.getLinkFunc().apply(obj), value);
                } else {
                    td.addText(value);
                }
            }
        }
        return table;
    }

    @Override
    public Element getBody() {
        return table;
    }

    @Override
    public List<String> createPlainList() {
        List<Entity> entities = getListOfEntities();
        List<String> list = new LinkedList<>();
        StringBuilder aux = new StringBuilder();
        for (String s : headings) {
            aux.append(String.format(plainFormat, s));
        }
        list.add(aux.toString());
        for (Entity e : entities) {
            aux = new StringBuilder();
            for (String s : e.getValues()) {
                aux.append(String.format(plainFormat, s));
            }
            list.add(aux.toString());
        }
        return list;
    }
}

