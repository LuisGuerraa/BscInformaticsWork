package pt.isel.ls.result;

import java.util.LinkedList;
import java.util.List;

import pt.isel.ls.model.Entity;
import pt.isel.ls.node.Element;

public class SingleResult extends Result {

    private final List<String> headings;
    private final Element list;

    @SafeVarargs
    public <T extends Entity> SingleResult(String titleOfResult,
                              List<T> list,
                              List<String> headings,
                              Property<T>... props) {

        super(titleOfResult, list);
        this.headings = headings;
        this.list = createList(list, props);
    }

    private <T> Element createList(List<T> list, Property<T>[] props) {
        Element ul = new Element("ul");
        int i = 0;
        T obj = list.get(0);
        for (String h : headings) {
            Element li = ul.addElem("li");
            String value = props[i].getFunc().apply(obj);
            if (props[i].hasLink()) {
                li.addLink(props[i].getLinkFunc().apply(obj), h + " : " + value);
            } else {
                li.addText(h + " : " + value);
            }
            i++;
        }
        return ul;
    }

    @Override
    public Element getBody() {
        return list;
    }

    @Override
    public List<String> createPlainList() {
        Entity e = getListOfEntities().get(0);
        List<String> values = e.getValues();
        List<String> list = new LinkedList<>();
        for (int i = 0; i < headings.size(); i++) {
            list.add(String.format(plainFormat, headings.get(i))
                    + String.format(plainFormat, values.get(i)));
        }
        return list;
    }

}
