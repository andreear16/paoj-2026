package com.pao.laboratory10.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    enum TipTranzactie {
        CREDIT,
        DEBIT
    }

    static class Tranzactie {
        private int id;
        private double suma;
        private String data;
        private TipTranzactie tip;
        private String contSursa;

        public Tranzactie(int id, double suma, String data, TipTranzactie tip, String contSursa) {
            this.id = id;
            this.suma = suma;
            this.data = data;
            this.tip = tip;
            this.contSursa = contSursa;
        }

        public int getId() {
            return id;
        }

        public double getSuma() {
            return suma;
        }

        public String getData() {
            return data;
        }

        public TipTranzactie getTip() {
            return tip;
        }

        public String getContSursa() {
            return contSursa;
        }

        public String getLuna() {
            return data.substring(0, 7);
        }

        @Override
        public String toString() {
            return String.format(Locale.US,
                    "[%d] %s %s: %.2f RON, cont sursa: %s",
                    id, data, tip, suma, contSursa);
        }
    }

    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe

        List<Tranzactie> tranzactii = new ArrayList<>();

        tranzactii.add(new Tranzactie(1, 1500.00, "2024-01-10", TipTranzactie.CREDIT, "CONT_A"));
        tranzactii.add(new Tranzactie(2, 200.00, "2024-01-12", TipTranzactie.DEBIT, "CONT_B"));
        tranzactii.add(new Tranzactie(3, 750.50, "2024-01-20", TipTranzactie.DEBIT, "CONT_A"));
        tranzactii.add(new Tranzactie(4, 2200.00, "2024-02-03", TipTranzactie.CREDIT, "CONT_C"));
        tranzactii.add(new Tranzactie(5, 120.75, "2024-02-11", TipTranzactie.DEBIT, "CONT_B"));
        tranzactii.add(new Tranzactie(6, 400.00, "2024-02-15", TipTranzactie.CREDIT, "CONT_D"));
        tranzactii.add(new Tranzactie(7, 900.00, "2024-03-01", TipTranzactie.DEBIT, "CONT_A"));
        tranzactii.add(new Tranzactie(8, 3000.00, "2024-03-07", TipTranzactie.CREDIT, "CONT_C"));
        tranzactii.add(new Tranzactie(9, 80.00, "2024-03-12", TipTranzactie.DEBIT, "CONT_E"));
        tranzactii.add(new Tranzactie(10, 650.00, "2024-04-02", TipTranzactie.CREDIT, "CONT_A"));

        System.out.println("1. Tranzactii CREDIT:");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println();

        System.out.println("2. Total procesat:");
        double totalProcesat = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();

        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", totalProcesat);

        System.out.println();

        System.out.println("3. Total pe luna:");
        Map<String, Double> totalPeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        Tranzactie::getLuna,
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));

        totalPeLuna.forEach((luna, total) ->
                System.out.printf(Locale.US, "%s: %.2f RON%n", luna, total));

        System.out.println();

        System.out.println("4. Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println();

        System.out.println("5. Conturi sursa unice:");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());

        System.out.println("Conturi sursa unice: " + conturiUnice);

        System.out.println();

        System.out.println("6. Suma medie:");
        double sumaMedie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);

        System.out.printf(Locale.US, "Suma medie: %.2f RON%n", sumaMedie);

        System.out.println();

        System.out.println("7. Extrase de cont lunare:");
        Map<String, List<Tranzactie>> tranzactiiPeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        Tranzactie::getLuna,
                        TreeMap::new,
                        Collectors.toList()
                ));

        tranzactiiPeLuna.forEach((luna, lista) -> {
            double totalLuna = lista.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();

            System.out.printf(Locale.US,
                    "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna,
                    lista.size(),
                    totalLuna);
        });
    }
}