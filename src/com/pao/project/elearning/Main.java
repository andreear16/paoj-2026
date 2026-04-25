package com.pao.project.elearning;
import com.pao.project.elearning.exception.CursNegasitException;
import com.pao.project.elearning.exception.UserNegasitException;
import com.pao.project.elearning.model.CodCurs;
import com.pao.project.elearning.model.Curs;
import com.pao.project.elearning.model.Cursant;
import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.model.Intrebare;
import com.pao.project.elearning.model.Lectie;
import com.pao.project.elearning.model.Quiz;
import com.pao.project.elearning.model.RezultatQuiz;
import com.pao.project.elearning.service.CursService;
import com.pao.project.elearning.service.QuizService;
import com.pao.project.elearning.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final UserService userService = UserService.getInstance();
    private static final CursService cursService = CursService.getInstance();
    private static final QuizService quizService = QuizService.getInstance();

    private static int nextCursantId = 4;
    private static int nextLectieId = 3;

    public static void main(String[] args) {
        initializeazaDate();

        int optiune;

        do {
            afiseazaMeniu();
            optiune = citesteInt("Alege optiunea: ");
            System.out.println();

            switch (optiune) {
                case 1:
                    afiseazaCursanti();
                    break;
                case 2:
                    afiseazaInstructori();
                    break;
                case 3:
                    afiseazaCursuri();
                    break;
                case 4:
                    afiseazaCursuriSortate();
                    break;
                case 5:
                    cautaCursDupaCod();
                    break;
                case 6:
                    inscrieCursantLaCurs();
                    break;
                case 7:
                    afiseazaCursantiInscrisiLaCurs();
                    break;
                case 8:
                    afiseazaLectiiCurs();
                    break;
                case 9:
                    afiseazaQuizuriCurs();
                    break;
                case 10:
                    afiseazaRezultateCursant();
                    break;
                case 11:
                    afiseazaClasamentQuiz();
                    break;
                case 12:
                    adaugaCursant();
                    break;
                case 13:
                    adaugaCurs();
                    break;
                case 14:
                    adaugaLectieLaCurs();
                    break;
                case 15:
                    adaugaRezultatQuiz();
                    break;
                case 16:
                    stergeCursant();
                    break;
                case 17:
                    stergeCurs();
                    break;
                case 18:
                    stergeInstructor();
                    break;
                case 0:
                    System.out.println("Program inchis.");
                    break;
                default:
                    System.out.println("Optiune invalida.");
                    break;
            }

            System.out.println();

        } while (optiune != 0);
    }

    private static void initializeazaDate() {
        Cursant cursant1 = new Cursant(1, "Ana Popescu", "ana@email.com", "ana.p", 2);
        Cursant cursant2 = new Cursant(2, "Mihai Ionescu", "mihai@email.com", "mihai.i", 1);
        Cursant cursant3 = new Cursant(3, "Elena Stan", "elena@email.com", "elena.s", 3);

        userService.adaugaCursant(cursant1);
        userService.adaugaCursant(cursant2);
        userService.adaugaCursant(cursant3);

        Instructor instructor1 = new Instructor(10, "Prof. Andrei Matei", "andrei@email.com", "andrei.m", "Programare Java");
        Instructor instructor2 = new Instructor(11, "Prof. Ioana Radu", "ioana@email.com", "ioana.r", "Baze de date");

        userService.adaugaInstructor(instructor1);
        userService.adaugaInstructor(instructor2);

        Curs curs1 = new Curs(new CodCurs("JAVA101"), "Introducere in Java", instructor1, 30);
        Curs curs2 = new Curs(new CodCurs("DB101"), "Baze de date", instructor2, 25);
        Curs curs3 = new Curs(new CodCurs("OOP101"), "Programare orientata pe obiecte", instructor1, 35);

        cursService.adaugaCurs(curs1);
        cursService.adaugaCurs(curs2);
        cursService.adaugaCurs(curs3);

        try {
            cursService.inscrieCursantLaCurs(cursant1, "JAVA101");
            cursService.inscrieCursantLaCurs(cursant2, "JAVA101");
            cursService.inscrieCursantLaCurs(cursant3, "DB101");

            cursService.adaugaLectieLaCurs("JAVA101", new Lectie(1, "Clase si obiecte", 60));
            cursService.adaugaLectieLaCurs("JAVA101", new Lectie(2, "Mostenire", 75));

            Quiz quizJava = new Quiz(1, "Quiz Java basic");
            quizJava.adaugaIntrebare(new Intrebare(1, "Ce este o clasa?", "Un sablon pentru obiecte", 10));
            quizJava.adaugaIntrebare(new Intrebare(2, "Ce inseamna extends?", "Mostenire", 10));

            cursService.adaugaQuizLaCurs("JAVA101", quizJava);

            quizService.adaugaRezultat(cursant1, quizJava, 95);
            quizService.adaugaRezultat(cursant2, quizJava, 80);

        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afiseazaMeniu() {
        System.out.println("=== Platforma e-learning ===");
        System.out.println("1. Afiseaza cursanti");
        System.out.println("2. Afiseaza instructori");
        System.out.println("3. Afiseaza cursuri");
        System.out.println("4. Afiseaza cursuri sortate dupa titlu");
        System.out.println("5. Cauta curs dupa cod");
        System.out.println("6. Inscrie cursant la curs");
        System.out.println("7. Afiseaza cursantii inscrisi la un curs");
        System.out.println("8. Afiseaza lectiile unui curs");
        System.out.println("9. Afiseaza quiz-urile unui curs");
        System.out.println("10. Afiseaza rezultatele unui cursant");
        System.out.println("11. Afiseaza clasamentul unui quiz");
        System.out.println("12. Adauga cursant");
        System.out.println("13. Adauga curs");
        System.out.println("14. Adauga lectie la curs");
        System.out.println("15. Adauga rezultat la quiz");
        System.out.println("16. Sterge cursant dupa id");
        System.out.println("17. Sterge curs dupa cod");
        System.out.println("18. Sterge instructor dupa id");
        System.out.println("0. Iesire");
    }

    private static void afiseazaCursanti() {
        System.out.println("Cursanti:");

        for (Cursant cursant : userService.getCursanti()) {
            System.out.println(cursant);
        }
    }

    private static void afiseazaInstructori() {
        System.out.println("Instructori:");

        for (Instructor instructor : userService.getInstructori()) {
            System.out.println(instructor);
        }
    }

    private static void afiseazaCursuri() {
        System.out.println("Cursuri:");

        for (Curs curs : cursService.getCursuri()) {
            System.out.println(curs);
        }
    }

    private static void afiseazaCursuriSortate() {
        System.out.println("Cursuri sortate:");

        for (Curs curs : cursService.getCursuriSortate()) {
            System.out.println(curs);
        }
    }

    private static void cautaCursDupaCod() {
        String cod = citesteString("Cod curs: ");

        try {
            Curs curs = cursService.cautaCursDupaCod(cod);
            System.out.println(curs);
        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inscrieCursantLaCurs() {
        int idCursant = citesteInt("Id cursant: ");
        String codCurs = citesteString("Cod curs: ");

        try {
            Cursant cursant = userService.cautaCursantDupaId(idCursant);
            cursService.inscrieCursantLaCurs(cursant, codCurs);
            System.out.println("Cursant inscris cu succes.");
        } catch (UserNegasitException e) {
            System.out.println(e.getMessage());
        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afiseazaCursantiInscrisiLaCurs() {
        String codCurs = citesteString("Cod curs: ");

        try {
            Set<Cursant> cursanti = cursService.getCursantiInscrisiLaCurs(codCurs);

            if (cursanti.isEmpty()) {
                System.out.println("Nu exista cursanti inscrisi la acest curs.");
            } else {
                for (Cursant cursant : cursanti) {
                    System.out.println(cursant);
                }
            }
        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afiseazaLectiiCurs() {
        String codCurs = citesteString("Cod curs: ");

        try {
            Curs curs = cursService.cautaCursDupaCod(codCurs);

            if (curs.getLectii().isEmpty()) {
                System.out.println("Cursul nu are lectii.");
            } else {
                for (Lectie lectie : curs.getLectii()) {
                    System.out.println(lectie);
                }
            }
        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afiseazaQuizuriCurs() {
        String codCurs = citesteString("Cod curs: ");

        try {
            Curs curs = cursService.cautaCursDupaCod(codCurs);

            if (curs.getQuizuri().isEmpty()) {
                System.out.println("Cursul nu are quiz-uri.");
            } else {
                for (Quiz quiz : curs.getQuizuri()) {
                    System.out.println(quiz);
                }
            }
        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afiseazaRezultateCursant() {
        int idCursant = citesteInt("Id cursant: ");

        List<RezultatQuiz> rezultate = quizService.getRezultateCursant(idCursant);

        if (rezultate.isEmpty()) {
            System.out.println("Cursantul nu are rezultate.");
        } else {
            for (RezultatQuiz rezultat : rezultate) {
                System.out.println(rezultat);
            }
        }
    }

    private static void afiseazaClasamentQuiz() {
        int idQuiz = citesteInt("Id quiz: ");

        List<RezultatQuiz> clasament = quizService.getClasamentQuiz(idQuiz);

        if (clasament.isEmpty()) {
            System.out.println("Nu exista rezultate pentru acest quiz.");
        } else {
            for (RezultatQuiz rezultat : clasament) {
                System.out.println(rezultat);
            }
        }
    }

    private static void adaugaCursant() {
        String nume = citesteString("Nume: ");
        String email = citesteString("Email: ");
        String username = citesteString("Username: ");
        int anStudiu = citesteInt("An studiu: ");

        Cursant cursant = new Cursant(nextCursantId, nume, email, username, anStudiu);
        nextCursantId++;

        try {
            userService.adaugaCursant(cursant);

            System.out.println("Cursant adaugat:");
            System.out.println(cursant);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adaugaCurs() {
        String cod = citesteString("Cod curs: ");
        String titlu = citesteString("Titlu curs: ");
        int durataOre = citesteInt("Durata in ore: ");
        int idInstructor = citesteInt("Id instructor: ");

        try {
            Instructor instructor = userService.cautaInstructorDupaId(idInstructor);

            Curs curs = new Curs(new CodCurs(cod), titlu, instructor, durataOre);
            cursService.adaugaCurs(curs);

            System.out.println("Curs adaugat:");
            System.out.println(curs);
        } catch (UserNegasitException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adaugaLectieLaCurs() {
        String codCurs = citesteString("Cod curs: ");
        String titlu = citesteString("Titlu lectie: ");
        int durataMinute = citesteInt("Durata in minute: ");

        Lectie lectie = new Lectie(nextLectieId, titlu, durataMinute);
        nextLectieId++;

        try {
            cursService.adaugaLectieLaCurs(codCurs, lectie);
            System.out.println("Lectie adaugata.");
        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adaugaRezultatQuiz() {
        int idCursant = citesteInt("Id cursant: ");
        int idQuiz = citesteInt("Id quiz: ");
        int scor = citesteInt("Scor: ");

        try {
            Cursant cursant = userService.cautaCursantDupaId(idCursant);
            Quiz quiz = cautaQuizDupaId(idQuiz);

            if (quiz == null) {
                System.out.println("Quiz-ul cu id-ul " + idQuiz + " nu a fost gasit.");
                return;
            }

            quizService.adaugaRezultat(cursant, quiz, scor);
            System.out.println("Rezultat adaugat.");
        } catch (UserNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void stergeCursant() {
        int idCursant = citesteInt("Id cursant: ");

        try {
            userService.cautaCursantDupaId(idCursant);

            quizService.stergeRezultateCursant(idCursant);
            cursService.stergeCursantDinCursuri(idCursant);
            userService.stergeCursantDupaId(idCursant);

            System.out.println("Cursant sters cu succes.");
        } catch (UserNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void stergeCurs() {
        String codCurs = citesteString("Cod curs: ");

        try {
            Curs curs = cursService.cautaCursDupaCod(codCurs);

            quizService.stergeRezultatePentruQuizuri(curs.getQuizuri());
            cursService.stergeCursDupaCod(codCurs);

            System.out.println("Curs sters cu succes.");
        } catch (CursNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void stergeInstructor() {
        int idInstructor = citesteInt("Id instructor: ");

        try {
            userService.stergeInstructorDupaId(idInstructor);
            System.out.println("Instructor sters cu succes.");
        } catch (UserNegasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Quiz cautaQuizDupaId(int idQuiz) {
        for (Curs curs : cursService.getCursuri()) {
            for (Quiz quiz : curs.getQuizuri()) {
                if (quiz.getId() == idQuiz) {
                    return quiz;
                }
            }
        }

        return null;
    }

    private static int citesteInt(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Trebuie sa introduci un numar.");
            }
        }
    }

    private static String citesteString(String mesaj) {
        System.out.print(mesaj);
        return scanner.nextLine();
    }
}