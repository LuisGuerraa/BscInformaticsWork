package pt.isel.ls.model.entities;

import java.util.List;

import pt.isel.ls.model.Entity;

public class Student extends Entity {

    private final int number;
    private final String email;
    private final String programme;

    public Student(int number, String email, String programme) {
        this.number = number;
        this.email = email;
        this.programme = programme;
    }

    public int getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getProgramme() {
        return programme;
    }

    @Override
    public List<String> getValues() {
        return List.of(String.valueOf(number), email, programme);
    }
}
