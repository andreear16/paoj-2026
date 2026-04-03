package com.pao.laboratory06.exercise3;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer i1 = new Inginer("Popescu", "Ana", "0711111111", 9000);
        Inginer i2 = new Inginer("Ionescu", "Vlad", "0722222222", 12000);
        Inginer i3 = new Inginer("Georgescu", "Mara", "0733333333", 10000);

        Inginer[] ingineri = {i1, i2, i3};

        System.out.println("sortare naturala dupa nume");
        Arrays.sort(ingineri);
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        System.out.println();
        System.out.println("sortare descrescatoare dupa salariu");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        System.out.println();
        System.out.println("acces prin referinta de interfata");
        PlataOnline contInginer = i1;
        contInginer.autentificare("ana_user", "1234");
        System.out.println("sold inginer: " + contInginer.consultareSold());
        System.out.println("plata 1500: " + contInginer.efectuarePlata(1500));
        System.out.println("sold dupa plata: " + contInginer.consultareSold());

        System.out.println();
        PersoanaJuridica firma1 = new PersoanaJuridica("Tech", "SRL", "0744444444", 50000);
        PersoanaJuridica firma2 = new PersoanaJuridica("NoPhone", "SRL", "", 30000);

        PlataOnlineSMS contFirma = firma1;
        contFirma.autentificare("tech_user", "abcd");
        System.out.println("sold firma: " + contFirma.consultareSold());
        System.out.println("plata 7000: " + contFirma.efectuarePlata(7000));
        System.out.println("sold dupa plata: " + contFirma.consultareSold());
        System.out.println("sms valid: " + contFirma.trimiteSMS("plata confirmata"));

        System.out.println();
        PlataOnlineSMS contFirmaFaraTelefon = firma2;
        System.out.println("sms fara telefon: " + contFirmaFaraTelefon.trimiteSMS("mesaj test"));
        System.out.println("sms gol: " + contFirma.trimiteSMS(""));

        System.out.println();
        System.out.println("mesaje trimise de firma1");
        for (String sms : firma1.getSmsTrimise()) {
            System.out.println(sms);
        }

        System.out.println();
        System.out.println("constanta financiara");
        System.out.println("tva = " + ConstanteFinanciare.TVA.getValoare());

        System.out.println();
        System.out.println("cazuri de eroare");

        try {
            contInginer.autentificare(null, "1234");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            contInginer.efectuarePlata(-10);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            trimiteSmsPeEntitateFaraSms(i2, "test");
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void trimiteSmsPeEntitateFaraSms(Object obiect, String mesaj) {
        if (mesaj == null || mesaj.isEmpty()) {
            throw new IllegalArgumentException("mesaj invalid");
        }

        if (!(obiect instanceof PlataOnlineSMS)) {
            throw new UnsupportedOperationException("entitatea nu are capabilitate sms");
        }

        ((PlataOnlineSMS) obiect).trimiteSMS(mesaj);
    }
}