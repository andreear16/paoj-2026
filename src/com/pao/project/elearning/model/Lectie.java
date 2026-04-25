package com.pao.project.elearning.model;

public class Lectie {
    private int id;
    private String titlu;
    private int durataMinute;

    public Lectie(int id, String titlu, int durataMinute) {
        this.id = id;
        this.titlu = titlu;
        this.durataMinute = durataMinute;
    }

    public int getId() {
        return id;
    }

    public String getTitlu() {
        return titlu;
    }

    public int getDurataMinute() {
        return durataMinute;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public void setDurataMinute(int durataMinute) {
        this.durataMinute = durataMinute;
    }

    @Override
    public String toString() {
        return "Lectie{id=" + id +
                ", titlu='" + titlu + '\'' +
                ", durataMinute=" + durataMinute +
                '}';
    }
}