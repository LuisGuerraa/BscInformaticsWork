package pt.isel.ls.model.entities;

import java.util.List;

import pt.isel.ls.model.Entity;

public class Programme extends Entity {

    private final String acr;
    private final String name;
    private final int length;

    public Programme(String acr, String name, int length) {
        this.acr = acr;
        this.name = name;
        this.length = length;
    }

    public String getAcr() {
        return acr;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    @Override
    public List<String> getValues() {
        return List.of(acr, name, String.valueOf(length));
    }
}
