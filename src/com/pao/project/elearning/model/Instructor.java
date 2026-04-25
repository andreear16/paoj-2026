package com.pao.project.elearning.model;
import java.util.Objects;

public class Instructor extends User {
    private String specializare;

    public Instructor(int id, String nume, String email, String username, String specializare) {
        super(id, nume, email, username);
        this.specializare = specializare;
    }

    @Override
    public String getRol() {
        return "Instructor";
    }

    public String getSpecializare() {
        return specializare;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }

    @Override
    public String toString() {
        return "Instructor{id=" + id +
                ", nume='" + nume + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", specializare='" + specializare + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instructor)) {
            return false;
        }

        Instructor instructor = (Instructor) o;
        return id == instructor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}