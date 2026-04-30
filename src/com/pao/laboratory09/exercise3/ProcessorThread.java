package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;

    private final CoadaTranzactii coada;
    private int totalProcesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            while (activ || !coada.esteGoala()) {
                Tranzactie tranzactie = coada.extrage();

                if (tranzactie == null) {
                    break;
                }

                Thread.sleep(80);
                totalProcesate++;

                System.out.printf(Locale.US, "[Processor] Factura #%d - %.2f RON | %s%n", tranzactie.getId(), tranzactie.getSuma(), tranzactie.getData());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getTotalProcesate() {
        return totalProcesate;
    }
}