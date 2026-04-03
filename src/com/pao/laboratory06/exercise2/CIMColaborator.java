package com.pao.laboratory06.exercise2;
import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        this.tip = TipColaborator.CIM;
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        bonus = false;
        if (in.hasNext()) {
            String s = in.next();
            bonus = s.equalsIgnoreCase("DA");
        }
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei%n",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venit = venitBrutLunar * 12 * 0.55;
        if (bonus) {
            venit = venit * 1.10;
        }
        return venit;
    }
}