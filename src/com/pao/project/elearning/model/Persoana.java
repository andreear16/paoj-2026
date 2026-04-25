package com.pao.project.elearning.model;

public abstract class Persoana {
    protected int id;
    protected String nume;
    protected String email;

    public Persoana(int id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    public abstract String getRol();

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getEmail() {
        return email;
    }
}