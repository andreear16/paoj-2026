package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti;

    private BibliotecaService() {
        carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] copie = new Carte[carti.length + 1];
        for (int i = 0; i < carti.length; i++) {
            copie[i] = carti[i];
        }
        copie[carti.length] = carte;
        carti = copie;
        System.out.println("Carte adăugată: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] copie = carti.clone();
        Arrays.sort(copie);
        for (int i = 0; i < copie.length; i++) {
            System.out.println((i + 1) + ". " + copie[i]);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copie = carti.clone();
        Arrays.sort(copie, comparator);
        for (int i = 0; i < copie.length; i++) {
            System.out.println((i + 1) + ". " + copie[i]);
        }
    }
}