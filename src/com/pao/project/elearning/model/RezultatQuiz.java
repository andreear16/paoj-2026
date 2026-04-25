package com.pao.project.elearning.model;

public class RezultatQuiz {
    private Cursant cursant;
    private Quiz quiz;
    private int scor;

    public RezultatQuiz(Cursant cursant, Quiz quiz, int scor) {
        this.cursant = cursant;
        this.quiz = quiz;
        this.scor = scor;
    }

    public Cursant getCursant() {
        return cursant;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public int getScor() {
        return scor;
    }

    public void setScor(int scor) {
        this.scor = scor;
    }

    @Override
    public String toString() {
        return "RezultatQuiz{cursant=" + cursant.getNume() +
                ", quiz=" + quiz.getTitlu() +
                ", scor=" + scor +
                '}';
    }
}