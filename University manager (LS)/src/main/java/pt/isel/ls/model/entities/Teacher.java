package pt.isel.ls.model.entities;

import java.util.List;

import pt.isel.ls.model.Entity;

public class Teacher extends Entity {

    private final String email;
    private final int num;

    public Teacher(int number, String email) {
        this.email = email;
        this.num = number;
    }

    public String getEmail() {
        return email;
    }

    public int getNum() {
        return num;
    }

    @Override
    public List<String> getValues() {
        return List.of(String.valueOf(num), email);
    }
}
