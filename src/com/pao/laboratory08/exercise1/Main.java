package com.pao.laboratory08.exercise1;
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        ArrayList<Student> studenti = readStudents();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String commandLine = br.readLine();

        if (commandLine == null) {
            return;
        }

        String[] parts = commandLine.trim().split(" ", 2);
        String command = parts[0];

        if (command.equals("PRINT")) {
            for (Student student : studenti) {
                System.out.println(student);
            }
        } else if (command.equals("SHALLOW")) {
            String nume = parts[1];

            Student original = findStudent(studenti, nume);
            Student clona = original.shallowClone();

            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        } else if (command.equals("DEEP")) {
            String nume = parts[1];

            Student original = findStudent(studenti, nume);
            Student clona = original.deepClone();

            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        }
    }

    private static ArrayList<Student> readStudents() throws IOException {
        ArrayList<Student> studenti = new ArrayList<>();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(FILE_PATH),
                        java.nio.charset.StandardCharsets.UTF_8
                )
        );

        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 4);

            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();

            Adresa adresa = new Adresa(oras, strada);
            Student student = new Student(nume, varsta, adresa);

            studenti.add(student);
        }

        br.close();

        return studenti;
    }

    private static Student findStudent(ArrayList<Student> studenti, String nume) {
        for (Student student : studenti) {
            if (student.getNume().equals(nume)) {
                return student;
            }
        }

        return null;
    }
}