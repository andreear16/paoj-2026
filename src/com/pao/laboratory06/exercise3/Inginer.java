package com.pao.laboratory06.exercise3;
public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private String user;
    private String parola;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon, salariu);
        this.autentificat = false;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("user sau parola invalida");
        }

        this.user = user;
        this.parola = parola;
        this.autentificat = true;
        System.out.println("autentificare reusita pentru inginerul " + getNumeComplet());
    }

    @Override
    public double consultareSold() {
        return salariu;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("suma invalida");
        }

        if (!autentificat) {
            return false;
        }

        if (salariu < suma) {
            return false;
        }

        salariu -= suma;
        return true;
    }

    @Override
    public int compareTo(Inginer other) {
        return this.nume.compareTo(other.nume);
    }

    @Override
    public String toString() {
        return "Inginer{nume='" + nume + "', prenume='" + prenume + "', salariu=" + salariu + "}";
    }
}