package com.pao.project.elearning.model;

public class Intrebare {
    private int id;
    private String text;
    private String raspunsCorect;
    private int punctaj;

    public Intrebare(int id, String text, String raspunsCorect, int punctaj) {
        this.id = id;
        this.text = text;
        this.raspunsCorect = raspunsCorect;
        this.punctaj = punctaj;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getRaspunsCorect() {
        return raspunsCorect;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRaspunsCorect(String raspunsCorect) {
        this.raspunsCorect = raspunsCorect;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }

    @Override
    public String toString() {
        return "Intrebare{id=" + id +
                ", text='" + text + '\'' +
                ", raspunsCorect='" + raspunsCorect + '\'' +
                ", punctaj=" + punctaj +
                '}';
    }
}