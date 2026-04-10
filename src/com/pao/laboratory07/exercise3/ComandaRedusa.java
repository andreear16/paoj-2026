package com.pao.laboratory07.exercise3;

public final class ComandaRedusa extends Comanda {
    private int discountProcent;

    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        super(nume, pret, client);
        this.discountProcent = discountProcent;
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }

    @Override
    public String descriere() {
        return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s] - client: %s",
                nume, pretFinal(), discountProcent, stare, client);
    }

    @Override
    public String descriereSimpla() {
        return String.format("DISCOUNTED: %s, pret: %.2f lei - client: %s",
                nume, pretFinal(), client);
    }

    public String descriereSpeciala() {
        return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) - client: %s",
                nume, pretFinal(), discountProcent, client);
    }

    public int getDiscountProcent() {
        return discountProcent;
    }
}