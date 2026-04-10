package com.pao.laboratory07.exercise3;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());

        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] t = line.split(" ");

            if (t[0].equals("STANDARD")) {
                String nume = t[1];
                double pret = Double.parseDouble(t[2]);
                String client = t[3];
                comenzi.add(new ComandaStandard(nume, pret, client));
            } else if (t[0].equals("DISCOUNTED")) {
                String nume = t[1];
                double pret = Double.parseDouble(t[2]);
                int discount = Integer.parseInt(t[3]);
                String client = t[4];
                comenzi.add(new ComandaRedusa(nume, pret, discount, client));
            } else if (t[0].equals("GIFT")) {
                String nume = t[1];
                String client = t[2];
                comenzi.add(new ComandaGratuita(nume, client));
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        System.out.println();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] t = line.split(" ");
            String cmd = t[0];

            if (cmd.equals("QUIT")) {
                return;
            }

            if (cmd.equals("STATS")) {
                System.out.println("--- STATS ---");

                Map<String, Double> medii = comenzi.stream()
                        .collect(Collectors.groupingBy(
                                c -> {
                                    if (c instanceof ComandaStandard) {
                                        return "STANDARD";
                                    }
                                    if (c instanceof ComandaRedusa) {
                                        return "DISCOUNTED";
                                    }
                                    return "GIFT";
                                },
                                LinkedHashMap::new,
                                Collectors.averagingDouble(Comanda::pretFinal)
                        ));

                if (medii.containsKey("STANDARD")) {
                    System.out.printf("STANDARD: medie = %.2f lei%n", medii.get("STANDARD"));
                }
                if (medii.containsKey("DISCOUNTED")) {
                    System.out.printf("DISCOUNTED: medie = %.2f lei%n", medii.get("DISCOUNTED"));
                }
                if (medii.containsKey("GIFT")) {
                    System.out.printf("GIFT: medie = %.2f lei%n", medii.get("GIFT"));
                }

                System.out.println();
            } else if (cmd.equals("FILTER")) {
                double prag = Double.parseDouble(t[1]);
                System.out.printf("--- FILTER (>= %.2f) ---%n", prag);

                comenzi.stream()
                        .filter(c -> c.pretFinal() >= prag)
                        .forEach(c -> System.out.println(c.descriereSimpla()));

                System.out.println();
            } else if (cmd.equals("SORT")) {
                System.out.println("--- SORT (by client, then by pret) ---");

                comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient)
                                .thenComparing(Comanda::pretFinal))
                        .forEach(c -> System.out.println(c.descriereSimpla()));

                System.out.println();
            } else if (cmd.equals("SPECIAL")) {
                System.out.println("--- SPECIAL (discount > 15%) ---");

                comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa)
                        .map(c -> (ComandaRedusa) c)
                        .filter(c -> c.getDiscountProcent() > 15)
                        .forEach(c -> System.out.println(c.descriereSpeciala()));

                System.out.println();
            }
        }
    }
}