package com.pao.project.elearning.service;
import com.pao.project.elearning.model.Cursant;
import com.pao.project.elearning.model.Quiz;
import com.pao.project.elearning.model.RezultatQuiz;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class QuizService {
    private static QuizService instance;

    private List<RezultatQuiz> rezultate;
    private Map<Integer, List<RezultatQuiz>> rezultateDupaCursant;
    private Map<Integer, List<RezultatQuiz>> rezultateDupaQuiz;

    private QuizService() {
        rezultate = new ArrayList<>();
        rezultateDupaCursant = new HashMap<>();
        rezultateDupaQuiz = new HashMap<>();
    }

    public static QuizService getInstance() {
        if (instance == null) {
            instance = new QuizService();
        }

        return instance;
    }

    public void adaugaRezultat(Cursant cursant, Quiz quiz, int scor) {
        RezultatQuiz rezultat = new RezultatQuiz(cursant, quiz, scor);

        rezultate.add(rezultat);

        rezultateDupaCursant.putIfAbsent(cursant.getId(), new ArrayList<>());
        rezultateDupaCursant.get(cursant.getId()).add(rezultat);

        rezultateDupaQuiz.putIfAbsent(quiz.getId(), new ArrayList<>());
        rezultateDupaQuiz.get(quiz.getId()).add(rezultat);
    }

    public List<RezultatQuiz> getRezultateCursant(int idCursant) {
        List<RezultatQuiz> lista = rezultateDupaCursant.get(idCursant);

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public List<RezultatQuiz> getClasamentQuiz(int idQuiz) {
        List<RezultatQuiz> lista = rezultateDupaQuiz.get(idQuiz);

        if (lista == null) {
            return new ArrayList<>();
        }

        List<RezultatQuiz> clasament = new ArrayList<>(lista);

        Collections.sort(clasament, new Comparator<RezultatQuiz>() {
            @Override
            public int compare(RezultatQuiz r1, RezultatQuiz r2) {
                return r2.getScor() - r1.getScor();
            }
        });

        return clasament;
    }

    public void stergeRezultateCursant(int idCursant) {
        List<RezultatQuiz> listaCursant = rezultateDupaCursant.remove(idCursant);

        if (listaCursant == null) {
            return;
        }

        for (RezultatQuiz rezultat : listaCursant) {
            rezultate.remove(rezultat);

            List<RezultatQuiz> listaQuiz = rezultateDupaQuiz.get(rezultat.getQuiz().getId());

            if (listaQuiz != null) {
                listaQuiz.remove(rezultat);

                if (listaQuiz.isEmpty()) {
                    rezultateDupaQuiz.remove(rezultat.getQuiz().getId());
                }
            }
        }
    }

    public void stergeRezultatePentruQuizuri(List<Quiz> quizuri) {
        for (Quiz quiz : quizuri) {
            List<RezultatQuiz> listaQuiz = rezultateDupaQuiz.remove(quiz.getId());

            if (listaQuiz != null) {
                for (RezultatQuiz rezultat : listaQuiz) {
                    rezultate.remove(rezultat);

                    List<RezultatQuiz> listaCursant = rezultateDupaCursant.get(rezultat.getCursant().getId());

                    if (listaCursant != null) {
                        listaCursant.remove(rezultat);

                        if (listaCursant.isEmpty()) {
                            rezultateDupaCursant.remove(rezultat.getCursant().getId());
                        }
                    }
                }
            }
        }
    }

    public List<RezultatQuiz> getRezultate() {
        return rezultate;
    }
}