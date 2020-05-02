package pt.isel.ls.model.entities;

import java.util.List;

import pt.isel.ls.model.Entity;

public class Course extends Entity {


    private final String acr;
    private final String name;
    private final int num;

    public Course(String acr, String name, int numTeacher) {
        this.acr = acr;
        this.name = name;
        this.num = numTeacher;
    }

    public int getNum() {
        return num;
    }

    public String getAcr() {
        return acr;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<String> getValues() {
        return List.of(acr,name,String.valueOf(num));
    }
}

