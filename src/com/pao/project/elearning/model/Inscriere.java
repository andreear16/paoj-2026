package com.pao.project.elearning.model;

public class Inscriere {
    private int id;
    private Cursant cursant;
    private Curs curs;
    private String dataInscriere;

    public Inscriere(int id, Cursant cursant, Curs curs, String dataInscriere) {
        this.id = id;
        this.cursant = cursant;
        this.curs = curs;
        this.dataInscriere = dataInscriere;
    }

    public int getId() {
        return id;
    }

    public Cursant getCursant() {
        return cursant;
    }

    public Curs getCurs() {
        return curs;
    }

    public String getDataInscriere() {
        return dataInscriere;
    }

    @Override
    public String toString() {
        return "Inscriere{id=" + id +
                ", cursant=" + cursant.getNume() +
                ", curs=" + curs.getTitlu() +
                ", dataInscriere='" + dataInscriere + '\'' +
                '}';
    }
}