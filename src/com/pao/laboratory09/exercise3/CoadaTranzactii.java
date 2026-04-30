package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private static final int CAPACITATE = 5;

    private final Queue<Tranzactie> tranzactii = new LinkedList<>();
    private boolean inchisa = false;

    public synchronized void adauga(Tranzactie tranzactie) throws InterruptedException {
        while (tranzactii.size() == CAPACITATE) {
            System.out.println("[ATM-" + tranzactie.getAtmId() + "] astept loc...");
            wait();
        }

        tranzactii.add(tranzactie);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (tranzactii.isEmpty() && !inchisa) {
            wait();
        }

        if (tranzactii.isEmpty()) {
            return null;
        }

        Tranzactie tranzactie = tranzactii.remove();
        notifyAll();

        return tranzactie;
    }

    public synchronized boolean esteGoala() {
        return tranzactii.isEmpty();
    }

    public synchronized void inchide() {
        inchisa = true;
        notifyAll();
    }
}