package com.pao.project.elearning.model;

public abstract class User extends Persoana {
    protected String username;

    public User(int id, String nume, String email, String username) {
        super(id, nume, email);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}