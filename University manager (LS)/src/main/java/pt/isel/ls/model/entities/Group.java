package pt.isel.ls.model.entities;

import java.util.List;

import pt.isel.ls.model.Entity;

public class Group extends Entity {

    private final int groupNumber;
    private final String classNumber;
    private final int year;
    private final String season;
    private final String acrCourse;

    public Group(int groupNumber, String classNumber,
                 int year, String season, String acrCourse) {
        this.groupNumber = groupNumber;
        this.classNumber = classNumber;
        this.year = year;
        this.season = season;
        this.acrCourse = acrCourse;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public int getYear() {
        return year;
    }

    public String getSeason() {
        return season;
    }

    public String getAcrCourse() {
        return acrCourse;
    }

    @Override
    public List<String> getValues() {
        return List.of(
                String.valueOf(groupNumber),classNumber,String.valueOf(year),season,acrCourse);
    }
}
