package com.pao.laboratory06.exercise3;
import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double sold;
    private String user;
    private String parola;
    private boolean autentificat;
    private List<String> smsTrimise;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
        this.autentificat = false;
        this.smsTrimise = new ArrayList<>();
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("user sau parola invalida");
        }

        this.user = user;
        this.parola = parola;
        this.autentificat = true;
        System.out.println("autentificare reusita pentru persoana juridica " + getNumeComplet());
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("suma invalida");
        }

        if (!autentificat) {
            return false;
        }

        if (sold < suma) {
            return false;
        }

        sold -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (telefon == null || telefon.isEmpty()) {
            return false;
        }

        if (mesaj == null || mesaj.isEmpty()) {
            return false;
        }

        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimieCopy();
    }

    private List<String> smsTrimieCopy() {
        return new ArrayList<>(smsTrimise);
    }

    @Override
    public String toString() {
        return "PersoanaJuridica{nume='" + nume + "', prenume='" + prenume + "', sold=" + sold + "}";
    }
}