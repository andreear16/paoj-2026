package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            EvenimentRepository repository = new EvenimentRepository();
            repository.initSchema();

            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.split("\\s+");
                String command = tokens[0];

                switch (command) {
                    case "ADD":
                        handleAdd(tokens, repository);
                        break;

                    case "LIST":
                        handleList(repository);
                        break;

                    case "DELETE":
                        handleDelete(tokens, repository);
                        break;

                    case "COUNT":
                        handleCount(repository);
                        break;

                    default:
                        break;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void handleAdd(String[] tokens, EvenimentRepository repository)
            throws SQLException {

        String nume = tokens[1];
        String data = tokens[2];
        int capacitate = Integer.parseInt(tokens[3]);
        String tip = tokens[4];

        Eveniment eveniment = new Eveniment(nume, data, capacitate, tip);

        repository.save(eveniment);

        System.out.println("Adaugat: [" + eveniment.getId() + "] "
                + eveniment.getNume());
    }

    private static void handleList(EvenimentRepository repository)
            throws SQLException {

        List<Eveniment> evenimente = repository.findAll();

        for (Eveniment eveniment : evenimente) {
            System.out.println(eveniment);
        }
    }

    private static void handleDelete(String[] tokens, EvenimentRepository repository)
            throws SQLException {

        int id = Integer.parseInt(tokens[1]);

        int deletedRows = repository.deleteImpl(id);

        if (deletedRows > 0) {
            System.out.println("Sters: " + id);
        } else {
            System.out.println("Nu exista: " + id);
        }
    }

    private static void handleCount(EvenimentRepository repository)
            throws SQLException {

        System.out.println("Total: " + repository.count());
    }
}