package com.pao.project.elearning.model;
import java.util.Objects;

public class Cursant extends User {
    private int anStudiu;

    public Cursant(int id, String nume, String email, String username, int anStudiu) {
        super(id, nume, email, username);
        this.anStudiu = anStudiu;
    }

    @Override
    public String getRol() {
        return "Cursant";
    }

    public int getAnStudiu() {
        return anStudiu;
    }

    public void setAnStudiu(int anStudiu) {
        this.anStudiu = anStudiu;
    }

    @Override
    public String toString() {
        return "Cursant{id=" + id +
                ", nume='" + nume + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", anStudiu=" + anStudiu +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cursant)) {
            return false;
        }

        Cursant cursant = (Cursant) o;
        return id == cursant.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}