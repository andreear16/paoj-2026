package com.pao.project.elearning.model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Curs implements Comparable<Curs> {
    private CodCurs cod;
    private String titlu;
    private Instructor instructor;
    private int durataOre;
    private List<Lectie> lectii;
    private List<Quiz> quizuri;
    private Set<Cursant> cursantiInscrisi;

    public Curs(CodCurs cod, String titlu, Instructor instructor, int durataOre) {
        this.cod = cod;
        this.titlu = titlu;
        this.instructor = instructor;
        this.durataOre = durataOre;
        this.lectii = new ArrayList<>();
        this.quizuri = new ArrayList<>();
        this.cursantiInscrisi = new HashSet<>();
    }

    public CodCurs getCod() {
        return cod;
    }

    public String getTitlu() {
        return titlu;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public int getDurataOre() {
        return durataOre;
    }

    public List<Lectie> getLectii() {
        return lectii;
    }

    public List<Quiz> getQuizuri() {
        return quizuri;
    }

    public Set<Cursant> getCursantiInscrisi() {
        return cursantiInscrisi;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void setDurataOre(int durataOre) {
        this.durataOre = durataOre;
    }

    public void adaugaLectie(Lectie lectie) {
        lectii.add(lectie);
    }

    public void adaugaQuiz(Quiz quiz) {
        quizuri.add(quiz);
    }

    public void inscrieCursant(Cursant cursant) {
        cursantiInscrisi.add(cursant);
    }

    @Override
    public int compareTo(Curs other) {
        int cmp = this.titlu.compareTo(other.titlu);

        if (cmp != 0) {
            return cmp;
        }

        return this.cod.getCod().compareTo(other.cod.getCod());
    }

    @Override
    public String toString() {
        return "Curs{cod=" + cod +
                ", titlu='" + titlu + '\'' +
                ", instructor=" + instructor.getNume() +
                ", durataOre=" + durataOre +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curs)) {
            return false;
        }

        Curs curs = (Curs) o;
        return Objects.equals(cod, curs.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }
}