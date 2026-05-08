package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

        Scanner scanner = new Scanner(System.in);
        ArrayList<Tranzactie> tranzactii = new ArrayList<>();

        if (!scanner.hasNextInt()) {
            scanner.close();
            return;
        }

        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            int id = Integer.parseInt(scanner.next());
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie tranzactie = new Tranzactie(id, suma, data, tip);
            tranzactii.add(tranzactie);
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            switch (comanda) {
                case "UNIQUE_IDS": {
                    LinkedHashSet<Integer> ids = new LinkedHashSet<>();

                    for (Tranzactie tranzactie : tranzactii) {
                        ids.add(tranzactie.getId());
                    }

                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                    break;
                }

                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> raport = new TreeMap<>();

                    for (Tranzactie tranzactie : tranzactii) {
                        String luna = tranzactie.getData().substring(0, 7);

                        if (!raport.containsKey(luna)) {
                            raport.put(luna, new double[2]);
                        }

                        double[] sume = raport.get(luna);

                        if (tranzactie.getTip() == TipTranzactie.CREDIT) {
                            sume[0] += tranzactie.getSuma();
                        } else {
                            sume[1] += tranzactie.getSuma();
                        }
                    }

                    for (Map.Entry<String, double[]> entry : raport.entrySet()) {
                        double[] sume = entry.getValue();

                        System.out.printf(Locale.US,
                                "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                entry.getKey(),
                                sume[0],
                                sume[1]);
                    }

                    break;
                }

                case "TOP": {
                    int nr = Integer.parseInt(scanner.next());
                    ArrayList<Tranzactie> copie = new ArrayList<>(tranzactii);

                    Collections.sort(copie, Comparator.comparingDouble(Tranzactie::getSuma).reversed());

                    System.out.println("Top " + nr + ":");

                    int limita = Math.min(nr, copie.size());
                    for (int i = 0; i < limita; i++) {
                        System.out.println(copie.get(i));
                    }

                    break;
                }

                case "SORT_ASC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    afiseazaLista(tranzactii);
                    break;
                }

                case "SORT_DESC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    afiseazaLista(tranzactii);
                    break;
                }

                case "REVERSE": {
                    Collections.reverse(tranzactii);
                    afiseazaLista(tranzactii);
                    break;
                }

                case "MIN_MAX": {
                    if (!tranzactii.isEmpty()) {
                        Comparator<Tranzactie> comparator = Comparator.comparingDouble(Tranzactie::getSuma);

                        Tranzactie minim = Collections.min(tranzactii, comparator);
                        Tranzactie maxim = Collections.max(tranzactii, comparator);

                        System.out.println("MIN: " + minim);
                        System.out.println("MAX: " + maxim);
                    }

                    break;
                }

                case "CME_DEMO": {
                    boolean prins = false;

                    try {
                        ArrayList<Tranzactie> copie = new ArrayList<>(tranzactii);

                        for (Tranzactie tranzactie : copie) {
                            copie.remove(tranzactie);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                        prins = true;
                    }

                    if (!prins) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }

                    break;
                }

                default:
                    break;
            }
        }

        scanner.close();
    }

    private static void afiseazaLista(List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie);
        }
    }
}