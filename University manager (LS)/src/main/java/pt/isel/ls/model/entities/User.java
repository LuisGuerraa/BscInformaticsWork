package pt.isel.ls.model.entities;

import java.util.List;

import pt.isel.ls.model.Entity;

public class User extends Entity {

    private final String email;
    private final String username;

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public List<String> getValues() {
        return List.of(email,username);
    }
}
