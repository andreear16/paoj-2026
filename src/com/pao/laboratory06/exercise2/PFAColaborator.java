package com.pao.laboratory06.exercise2;
import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double cheltuieliLunare;
    private static final double SALARIU_MINIM_BRUT_ANUAL = 4050 * 12;

    public PFAColaborator() {
        this.tip = TipColaborator.PFA;
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei%n",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;
        double impozit = 0.10 * venitNet;
        double cass;
        double cas;

        if (venitNet < 6 * SALARIU_MINIM_BRUT_ANUAL) {
            cass = 0.10 * (6 * SALARIU_MINIM_BRUT_ANUAL);
        } else if (venitNet <= 72 * SALARIU_MINIM_BRUT_ANUAL) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_BRUT_ANUAL);
        }

        if (venitNet < 12 * SALARIU_MINIM_BRUT_ANUAL) {
            cas = 0;
        } else if (venitNet <= 24 * SALARIU_MINIM_BRUT_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_BRUT_ANUAL);
        } else {
            cas = 0.25 * (24 * SALARIU_MINIM_BRUT_ANUAL);
        }

        return venitNet - impozit - cass - cas;
    }
}