package com.pao.laboratory08.exercise2;
import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "rezultate.txt";

    public static void main(String[] args) throws Exception {
        ArrayList<Student> studenti = readStudents();

        Scanner scanner = new Scanner(System.in);
        int prag = scanner.nextInt();

        ArrayList<Student> filtrati = new ArrayList<>();

        for (Student student : studenti) {
            if (student.getVarsta() >= prag) {
                filtrati.add(student);
            }
        }

        writeStudents(filtrati);

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();

        for (Student student : filtrati) {
            System.out.println(student);
        }

        System.out.println();
        System.out.println("Scris in: rezultate.txt");
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

    private static void writeStudents(ArrayList<Student> studenti) throws IOException {
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(OUTPUT_FILE),
                        java.nio.charset.StandardCharsets.UTF_8
                )
        );

        for (Student student : studenti) {
            bw.write(student.toString());
            bw.newLine();
        }

        bw.close();
    }
}