package com.pao.laboratory14.exercise1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collector;

public class Main {

    enum TipBilet {
        BACKSTAGE, STANDARD, VIP
    }

    static class Bilet {
        private final int id;
        private final String eveniment;
        private final TipBilet tip;
        private final double pret;

        public Bilet(int id, String eveniment, TipBilet tip, double pret) {
            this.id = id;
            this.eveniment = eveniment;
            this.tip = tip;
            this.pret = pret;
        }

        public int getId() {
            return id;
        }

        public String getEveniment() {
            return eveniment;
        }

        public TipBilet getTip() {
            return tip;
        }

        public double getPret() {
            return pret;
        }
    }

    static class RaportVanzari {
        private final Map<TipBilet, Long> numarPerTip;
        private final Map<TipBilet, Double> incasariPerTip;
        private final double totalGlobal;
        private final double medieGlobala;
        private final TipBilet tipCelMaiPopular;

        public RaportVanzari(
                Map<TipBilet, Long> numarPerTip,
                Map<TipBilet, Double> incasariPerTip,
                double totalGlobal,
                double medieGlobala,
                TipBilet tipCelMaiPopular
        ) {
            this.numarPerTip = Collections.unmodifiableMap(new EnumMap<>(numarPerTip));
            this.incasariPerTip = Collections.unmodifiableMap(new EnumMap<>(incasariPerTip));
            this.totalGlobal = totalGlobal;
            this.medieGlobala = medieGlobala;
            this.tipCelMaiPopular = tipCelMaiPopular;
        }

        public Map<TipBilet, Long> getNumarPerTip() {
            return numarPerTip;
        }

        public Map<TipBilet, Double> getIncasariPerTip() {
            return incasariPerTip;
        }

        public double getTotalGlobal() {
            return totalGlobal;
        }

        public double getMedieGlobala() {
            return medieGlobala;
        }

        public TipBilet getTipCelMaiPopular() {
            return tipCelMaiPopular;
        }
    }

    static class AcumulatorRaport {
        private final Map<TipBilet, Long> numarPerTip = new EnumMap<>(TipBilet.class);
        private final Map<TipBilet, Double> incasariPerTip = new EnumMap<>(TipBilet.class);

        public void adauga(Bilet bilet) {
            TipBilet tip = bilet.getTip();

            numarPerTip.put(tip, numarPerTip.getOrDefault(tip, 0L) + 1);
            incasariPerTip.put(tip, incasariPerTip.getOrDefault(tip, 0.0) + bilet.getPret());
        }

        public AcumulatorRaport combina(AcumulatorRaport other) {
            for (TipBilet tip : TipBilet.values()) {
                long count = this.numarPerTip.getOrDefault(tip, 0L)
                        + other.numarPerTip.getOrDefault(tip, 0L);

                double suma = this.incasariPerTip.getOrDefault(tip, 0.0)
                        + other.incasariPerTip.getOrDefault(tip, 0.0);

                if (count > 0) {
                    this.numarPerTip.put(tip, count);
                    this.incasariPerTip.put(tip, suma);
                }
            }

            return this;
        }

        public RaportVanzari finalizeaza() {
            double totalGlobal = 0.0;
            long totalBilete = 0;

            for (TipBilet tip : TipBilet.values()) {
                totalGlobal += incasariPerTip.getOrDefault(tip, 0.0);
                totalBilete += numarPerTip.getOrDefault(tip, 0L);
            }

            double medieGlobala;

            if (totalBilete == 0) {
                medieGlobala = 0.0;
            } else {
                medieGlobala = totalGlobal / totalBilete;
            }

            TipBilet tipCelMaiPopular = null;
            long maxCount = -1;

            for (TipBilet tip : TipBilet.values()) {
                long count = numarPerTip.getOrDefault(tip, 0L);

                if (count > maxCount) {
                    maxCount = count;
                    tipCelMaiPopular = tip;
                }
            }

            return new RaportVanzari(
                    numarPerTip,
                    incasariPerTip,
                    totalGlobal,
                    medieGlobala,
                    tipCelMaiPopular
            );
        }
    }

    private static Collector<Bilet, AcumulatorRaport, RaportVanzari> raportVanzariCollector() {
        return Collector.of(
                AcumulatorRaport::new,
                AcumulatorRaport::adauga,
                AcumulatorRaport::combina,
                AcumulatorRaport::finalizeaza
        );
    }

    private static void afiseazaRaportSimplu(RaportVanzari raport) {
        for (TipBilet tip : TipBilet.values()) {
            if (raport.getNumarPerTip().containsKey(tip)) {
                long count = raport.getNumarPerTip().get(tip);
                double incasari = raport.getIncasariPerTip().get(tip);

                System.out.printf("%s: count=%d incasari=%.2f RON%n",
                        tip.name(), count, incasari);
            }
        }
    }

    private static void afiseazaRaportComplet(RaportVanzari raport) {
        afiseazaRaportSimplu(raport);

        System.out.println("---");
        System.out.printf("Total: %.2f RON%n", raport.getTotalGlobal());
        System.out.printf("Medie: %.2f RON%n", raport.getMedieGlobala());
        System.out.println("Cel mai popular: " + raport.getTipCelMaiPopular().name());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine().trim());

        List<Bilet> bilete = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine().trim();
            String[] tokens = line.split("\\s+");

            int id = Integer.parseInt(tokens[0]);
            String eveniment = tokens[1];
            TipBilet tip = TipBilet.valueOf(tokens[2]);
            double pret = Double.parseDouble(tokens[3]);

            bilete.add(new Bilet(id, eveniment, tip, pret));
        }

        String comanda = scanner.nextLine().trim();

        RaportVanzari raport = bilete.stream()
                .collect(raportVanzariCollector());

        if (comanda.equals("RAPORT_SIMPLU")) {
            afiseazaRaportSimplu(raport);
        } else if (comanda.equals("RAPORT_COMPLET")) {
            afiseazaRaportComplet(raport);
        }
    }
}