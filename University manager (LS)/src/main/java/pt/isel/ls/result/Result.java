package pt.isel.ls.result;

import java.util.List;

import pt.isel.ls.model.Entity;
import pt.isel.ls.node.Element;

public abstract class Result {
    private List<Entity> list;
    private String header;
    final String plainFormat = "%-45s";

    public <T extends Entity> Result(String header, List<T> list) {
        this.header = header;
        this.list = (List<Entity>) list;
    }


    public List<Entity> getListOfEntities() {
        return list;
    }

    public String getHeader() {
        return header;
    }

    public abstract Element getBody();

    public abstract List<String> createPlainList();

}
