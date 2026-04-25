package com.pao.project.elearning.service;
import com.pao.project.elearning.exception.CursNegasitException;
import com.pao.project.elearning.model.Curs;
import com.pao.project.elearning.model.Cursant;
import com.pao.project.elearning.model.Inscriere;
import com.pao.project.elearning.model.Lectie;
import com.pao.project.elearning.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class CursService {
    private static CursService instance;

    private List<Curs> cursuri;
    private Set<Curs> cursuriSortate;
    private Map<String, Curs> cursuriDupaCod;
    private List<Inscriere> inscrieri;
    private int nextInscriereId;

    private CursService() {
        cursuri = new ArrayList<>();
        cursuriSortate = new TreeSet<>();
        cursuriDupaCod = new HashMap<>();
        inscrieri = new ArrayList<>();
        nextInscriereId = 1;
    }

    public static CursService getInstance() {
        if (instance == null) {
            instance = new CursService();
        }

        return instance;
    }

    public void adaugaCurs(Curs curs) {
        String cod = curs.getCod().getCod();

        if (cursuriDupaCod.containsKey(cod)) {
            throw new IllegalArgumentException("Exista deja un curs cu codul " + cod + ".");
        }

        cursuri.add(curs);
        cursuriSortate.add(curs);
        cursuriDupaCod.put(cod, curs);
    }

    public Curs cautaCursDupaCod(String cod) throws CursNegasitException {
        Curs curs = cursuriDupaCod.get(cod);

        if (curs == null) {
            throw new CursNegasitException("Cursul cu codul " + cod + " nu a fost gasit.");
        }

        return curs;
    }

    public void stergeCursDupaCod(String cod) throws CursNegasitException {
        Curs curs = cautaCursDupaCod(cod);

        cursuri.remove(curs);
        cursuriSortate.remove(curs);
        cursuriDupaCod.remove(cod);

        List<Inscriere> inscrieriRamase = new ArrayList<>();

        for (Inscriere inscriere : inscrieri) {
            if (!inscriere.getCurs().getCod().getCod().equals(cod)) {
                inscrieriRamase.add(inscriere);
            }
        }

        inscrieri = inscrieriRamase;
    }

    public void stergeCursantDinCursuri(int idCursant) {
        for (Curs curs : cursuri) {
            Cursant cursantGasit = null;

            for (Cursant cursant : curs.getCursantiInscrisi()) {
                if (cursant.getId() == idCursant) {
                    cursantGasit = cursant;
                    break;
                }
            }

            if (cursantGasit != null) {
                curs.getCursantiInscrisi().remove(cursantGasit);
            }
        }

        List<Inscriere> inscrieriRamase = new ArrayList<>();

        for (Inscriere inscriere : inscrieri) {
            if (inscriere.getCursant().getId() != idCursant) {
                inscrieriRamase.add(inscriere);
            }
        }

        inscrieri = inscrieriRamase;
    }

    public List<Curs> getCursuri() {
        return cursuri;
    }

    public Set<Curs> getCursuriSortate() {
        return cursuriSortate;
    }

    public void inscrieCursantLaCurs(Cursant cursant, String codCurs) throws CursNegasitException {
        Curs curs = cautaCursDupaCod(codCurs);

        if (curs.getCursantiInscrisi().contains(cursant)) {
            throw new IllegalArgumentException("Cursantul este deja inscris la cursul " + codCurs + ".");
        }

        curs.inscrieCursant(cursant);

        Inscriere inscriere = new Inscriere(nextInscriereId, cursant, curs, "2026-04-24");
        inscrieri.add(inscriere);

        nextInscriereId++;
    }

    public Set<Cursant> getCursantiInscrisiLaCurs(String codCurs) throws CursNegasitException {
        Curs curs = cautaCursDupaCod(codCurs);
        return curs.getCursantiInscrisi();
    }

    public void adaugaLectieLaCurs(String codCurs, Lectie lectie) throws CursNegasitException {
        Curs curs = cautaCursDupaCod(codCurs);
        curs.adaugaLectie(lectie);
    }

    public void adaugaQuizLaCurs(String codCurs, Quiz quiz) throws CursNegasitException {
        Curs curs = cautaCursDupaCod(codCurs);
        curs.adaugaQuiz(quiz);
    }

    public List<Inscriere> getInscrieri() {
        return inscrieri;
    }
}