package com.pao.laboratory09.exercise1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactie.setNote("procesat");

            tranzactii.add(tranzactie);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            out.writeObject(tranzactii);
        }

        List<Tranzactie> tranzactiiCitite;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            tranzactiiCitite = citesteLista(in);
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            if (comanda.equals("LIST")) {
                for (Tranzactie tranzactie : tranzactiiCitite) {
                    System.out.println(tranzactie);
                }
            } else if (comanda.equals("FILTER")) {
                String luna = scanner.next();
                boolean gasit = false;

                for (Tranzactie tranzactie : tranzactiiCitite) {
                    if (tranzactie.getData().startsWith(luna)) {
                        System.out.println(tranzactie);
                        gasit = true;
                    }
                }

                if (!gasit) {
                    System.out.println("Niciun rezultat.");
                }
            } else if (comanda.equals("NOTE")) {
                int id = scanner.nextInt();
                Tranzactie cautata = null;

                for (Tranzactie tranzactie : tranzactiiCitite) {
                    if (tranzactie.getId() == id) {
                        cautata = tranzactie;
                        break;
                    }
                }

                if (cautata == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + cautata.getNote());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Tranzactie> citesteLista(ObjectInputStream in) throws Exception {
        return (List<Tranzactie>) in.readObject();
    }
}