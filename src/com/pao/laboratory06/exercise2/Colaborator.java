package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;
    protected TipColaborator tip;

    public Colaborator() {
    }

    public Colaborator(String nume, String prenume, double venitBrutLunar, TipColaborator tip) {
        this.nume = nume;
        this.prenume = prenume;
        this.venitBrutLunar = venitBrutLunar;
        this.tip = tip;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public double getVenitBrutLunar() {
        return venitBrutLunar;
    }

    public TipColaborator getTip() {
        return tip;
    }

    public abstract double calculeazaVenitNetAnual();
}