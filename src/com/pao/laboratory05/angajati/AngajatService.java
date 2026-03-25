package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;

    private AngajatService() {
        angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] copie = new Angajat[angajati.length + 1];
        for (int i = 0; i < angajati.length; i++) {
            copie[i] = angajati[i];
        }
        copie[angajati.length] = a;
        angajati = copie;
        System.out.println("Angajat adăugat: " + a.getNume());
    }

    public void printAll() {
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        Angajat[] copie = angajati.clone();
        Arrays.sort(copie);
        for (int i = 0; i < copie.length; i++) {
            System.out.println((i + 1) + ". " + copie[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean gasit = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                if (!gasit) {
                    System.out.println("--- Angajați din " + numeDept + " ---");
                }
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}