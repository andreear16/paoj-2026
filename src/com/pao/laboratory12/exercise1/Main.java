package com.pao.laboratory12.exercise1;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.model.Loan;
import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.repository.LoanRepository;
import com.pao.laboratory12.repository.ReaderRepository;
import com.pao.laboratory12.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        try {
            // cream tabelele pentru test
            runSchema();

            AuthorRepository authorRepository = new AuthorRepository();
            BookRepository bookRepository = new BookRepository();
            ReaderRepository readerRepository = new ReaderRepository();
            LoanRepository loanRepository = new LoanRepository();

            System.out.println("=== CREATE ===");

            Author author = new Author("George Orwell", "United Kingdom");
            authorRepository.save(author);
            System.out.println("Autor salvat: " + author);

            Book book = new Book("1984", author.getId());
            bookRepository.save(book);
            System.out.println("Carte salvata: " + book);

            Reader reader = new Reader("Ion Popescu", "ion.popescu@email.com");
            readerRepository.save(reader);
            System.out.println("Cititor salvat: " + reader);

            Loan loan = new Loan(book.getId(), reader.getId(), "2026-05-24");
            loanRepository.save(loan);
            System.out.println("Imprumut salvat: " + loan);

            System.out.println();
            System.out.println("=== READ ALL ===");

            System.out.println("Autori: " + authorRepository.findAll());
            System.out.println("Carti: " + bookRepository.findAll());
            System.out.println("Cititori: " + readerRepository.findAll());
            System.out.println("Imprumuturi: " + loanRepository.findAll());

            System.out.println();
            System.out.println("=== FIND BY ID ===");

            System.out.println("Autor gasit: " + authorRepository.findById(author.getId()));
            System.out.println("Carte gasita: " + bookRepository.findById(book.getId()));
            System.out.println("Cititor gasit: " + readerRepository.findById(reader.getId()));
            System.out.println("Imprumut gasit: " + loanRepository.findById(loan.getId()));

            System.out.println();
            System.out.println("=== UPDATE ===");

            author.setCountry("UK");
            authorRepository.update(author);
            System.out.println("Autor dupa update: " + authorRepository.findById(author.getId()));

            book.setAvailable(false);
            bookRepository.update(book);
            System.out.println("Carte dupa update: " + bookRepository.findById(book.getId()));

            reader.setEmail("ion.nou@email.com");
            readerRepository.update(reader);
            System.out.println("Cititor dupa update: " + readerRepository.findById(reader.getId()));

            loan.setReturnDate("2026-06-01");
            loanRepository.update(loan);
            System.out.println("Imprumut dupa update: " + loanRepository.findById(loan.getId()));

            System.out.println();
            System.out.println("=== DELETE ===");

            // stergem in ordinea corecta, ca sa nu incalcam foreign key-urile
            loanRepository.delete(loan.getId());
            bookRepository.delete(book.getId());
            readerRepository.delete(reader.getId());
            authorRepository.delete(author.getId());

            System.out.println("Autori ramasi: " + authorRepository.findAll());
            System.out.println("Carti ramase: " + bookRepository.findAll());
            System.out.println("Cititori ramasi: " + readerRepository.findAll());
            System.out.println("Imprumuturi ramase: " + loanRepository.findAll());

            DatabaseConnection.getInstance().close();

            System.out.println();
            System.out.println("Exercise 1 terminat cu succes.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runSchema() throws Exception {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        String[] statements = {
                "DROP TABLE IF EXISTS loan",
                "DROP TABLE IF EXISTS book",
                "DROP TABLE IF EXISTS reader",
                "DROP TABLE IF EXISTS author",

                "CREATE TABLE author (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(200) NOT NULL, " +
                        "country VARCHAR(100)" +
                        ")",

                "CREATE TABLE book (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title VARCHAR(300) NOT NULL, " +
                        "author_id INTEGER NOT NULL, " +
                        "available INTEGER NOT NULL DEFAULT 1, " +
                        "FOREIGN KEY (author_id) REFERENCES author(id)" +
                        ")",

                "CREATE TABLE reader (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(200) NOT NULL, " +
                        "email VARCHAR(200)" +
                        ")",

                "CREATE TABLE loan (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "book_id INTEGER NOT NULL, " +
                        "reader_id INTEGER NOT NULL, " +
                        "loan_date VARCHAR(20) NOT NULL, " +
                        "return_date VARCHAR(20), " +
                        "FOREIGN KEY (book_id) REFERENCES book(id), " +
                        "FOREIGN KEY (reader_id) REFERENCES reader(id)" +
                        ")"
        };

        try (Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        }
    }
}