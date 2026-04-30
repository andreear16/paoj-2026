package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ATMThread extends Thread {
    private static int urmatorulId = 1;

    private final int atmId;
    private final CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 4; i++) {
                int idTranzactie = genereazaId();
                double suma = atmId * 100 + i * 25.5;
                String data = "2024-01-" + String.format("%02d", idTranzactie);

                Tranzactie tranzactie = new Tranzactie(idTranzactie, suma, data, atmId);

                coada.adauga(tranzactie);

                System.out.printf(Locale.US,
                        "[ATM-%d] trimite: Tranzactie #%d %.2f RON%n",
                        atmId, idTranzactie, suma);

                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static synchronized int genereazaId() {
        return urmatorulId++;
    }
}