package pt.isel.ls.model.entities;

import java.util.List;

import pt.isel.ls.model.Entity;

public class Class extends Entity {
    private final String id;
    private final int yearSem;
    private final String season;
    private final String acrCourse;

    public Class(String id, int yearSem, String season, String acrCourse) {
        this.id = id;
        this.yearSem = yearSem;
        this.season = season;
        this.acrCourse = acrCourse;
    }

    public String getId() {
        return id;
    }

    public int getYearSem() {
        return yearSem;
    }

    public String getSeason() {
        return season;
    }

    public String getAcrCourse() {
        return acrCourse;
    }

    @Override
    public List<String> getValues() {
        return List.of(id, String.valueOf(yearSem), season, acrCourse);
    }
}
