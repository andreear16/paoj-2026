package com.pao.laboratory06.exercise2;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c;

            if (tip.equals("CIM")) {
                c = new CIMColaborator();
            } else if (tip.equals("PFA")) {
                c = new PFAColaborator();
            } else {
                c = new SRLColaborator();
            }

            c.citeste(in);
            colaboratori.add(c);
        }

        for (TipColaborator tip : TipColaborator.values()) {
            List<Colaborator> lista = new ArrayList<>();

            for (Colaborator c : colaboratori) {
                if (c.getTip() == tip) {
                    lista.add(c);
                }
            }

            lista.sort(new Comparator<Colaborator>() {
                @Override
                public int compare(Colaborator a, Colaborator b) {
                    return Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual());
                }
            });

            for (Colaborator c : lista) {
                c.afiseaza();
            }
        }

        System.out.println();

        Colaborator maxim = null;
        for (Colaborator c : colaboratori) {
            if (maxim == null || c.calculeazaVenitNetAnual() > maxim.calculeazaVenitNetAnual()) {
                maxim = c;
            }
        }

        System.out.print("Colaborator cu venit net maxim: ");
        if (maxim != null) {
            maxim.afiseaza();
        }

        System.out.println();
        System.out.println("Colaboratori persoane juridice:");

        List<Colaborator> juridice = new ArrayList<>();
        for (Colaborator c : colaboratori) {
            if (c instanceof PersoanaJuridica) {
                juridice.add(c);
            }
        }

        juridice.sort(new Comparator<Colaborator>() {
            @Override
            public int compare(Colaborator a, Colaborator b) {
                return Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual());
            }
        });

        for (Colaborator c : juridice) {
            c.afiseaza();
        }

        System.out.println();
        System.out.println("Sume și număr colaboratori pe tip:");

        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);

        for (TipColaborator tip : TipColaborator.values()) {
            suma.put(tip, 0.0);
            numar.put(tip, 0);
        }

        for (Colaborator c : colaboratori) {
            TipColaborator tip = c.getTip();
            suma.put(tip, suma.get(tip) + c.calculeazaVenitNetAnual());
            numar.put(tip, numar.get(tip) + 1);
        }

        for (TipColaborator tip : TipColaborator.values()) {
            if (numar.get(tip) == 0) {
                System.out.printf("%s: suma = nu lei, număr = null%n", tip);
            } else {
                System.out.printf("%s: suma = %.2f lei, număr = %d%n",
                        tip, suma.get(tip), numar.get(tip));
            }
        }
    }
}