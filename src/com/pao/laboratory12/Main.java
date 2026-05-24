package com.pao.laboratory12;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.repository.LoanRepository;
import com.pao.laboratory12.repository.ReaderRepository;
import com.pao.laboratory12.service.AuditService;
import com.pao.laboratory12.service.LibraryService;
import com.pao.laboratory12.util.DatabaseConnection;
import com.pao.laboratory12.util.SchemaInitializer;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            SchemaInitializer.init(connection);

            AuditService audit = AuditService.getInstance();

            AuthorRepository authorRepo = new AuthorRepository();
            BookRepository bookRepo = new BookRepository();
            ReaderRepository readerRepo = new ReaderRepository();
            LoanRepository loanRepo = new LoanRepository();

            LibraryService libraryService = LibraryService.getInstance();

            System.out.println("=== BIBLIOTECA JDBC - Demo Lab12 ===");
            System.out.println();

            // 1. adauga autor
            Author author = new Author("Gabriel Garcia Marquez", "CO");
            authorRepo.save(author);
            audit.log("add_author");
            System.out.println("1. Autor adaugat: " + author);

            // 2. adauga carti
            Book book1 = new Book("100 de ani de singuratate", author.getId());
            Book book2 = new Book("Dragostea in vremea holerei", author.getId());

            bookRepo.save(book1);
            bookRepo.save(book2);

            audit.log("add_book");
            System.out.println("2. Carti adaugate: " + book1 + ", " + book2);

            // 3. adauga cititori
            Reader reader = new Reader("Ion Popescu", "ion.popescu@email.com");

            // cititorul acesta este folosit doar pentru delete,
            // ca sa nu stergem un cititor care este referit deja de loan
            Reader readerToDelete = new Reader("Maria Ionescu", "maria.ionescu@email.com");

            readerRepo.save(reader);
            readerRepo.save(readerToDelete);

            audit.log("add_reader");
            System.out.println("3. Cititori adaugati: " + reader + ", " + readerToDelete);

            // 4. listeaza toate cartile
            List<Book> allBooks = bookRepo.findAll();
            audit.log("list_books");

            System.out.println("4. Toate cartile (" + allBooks.size() + "):");
            for (Book book : allBooks) {
                System.out.println("   " + book);
            }

            // 5. cauta carte dupa id
            System.out.println("5. Cautare carte dupa id:");

            bookRepo.findById(book1.getId()).ifPresentOrElse(
                    book -> System.out.println("   Carte gasita: " + book),
                    () -> System.out.println("   Carte negasita.")
            );

            audit.log("find_book_by_id");

            // 6. actualizeaza carte
            book1.setTitle("100 de ani de singuratate (Ed. speciala)");
            bookRepo.update(book1);
            audit.log("update_book");

            System.out.println("6. Carte actualizata: " + bookRepo.findById(book1.getId()));

            // 7. imprumuta carte - tranzactie
            long loanId = libraryService.borrowBook(reader.getId(), book1.getId());
            audit.log("borrow_book");

            System.out.println("7. Imprumut creat cu ID=" + loanId);

            // 8. returneaza carte - tranzactie
            libraryService.returnBook(loanId);
            audit.log("return_book");

            System.out.println("8. Carte returnata.");

            // 9. raport cu JOIN
            List<String> activeLoans = libraryService.getActiveLoansWithDetails();
            audit.log("report_active_loans");

            System.out.println("9. Imprumuturi active:");
            if (activeLoans.isEmpty()) {
                System.out.println("   niciun");
            } else {
                for (String loan : activeLoans) {
                    System.out.println("   " + loan);
                }
            }

            System.out.println();
            System.out.println("Raport JOIN - top carti imprumutate:");
            for (String line : libraryService.getTopBorrowedBooksWithAuthor()) {
                System.out.println("   " + line);
            }

            System.out.println();
            System.out.println("Raport JOIN - imprumuturi per cititor:");
            for (String line : libraryService.getLoansCountPerReader()) {
                System.out.println("   " + line);
            }

            // 10. sterge cititor
            readerRepo.delete(readerToDelete.getId());
            audit.log("delete_reader");

            System.out.println();
            System.out.println("10. Cititor sters cu ID=" + readerToDelete.getId());

            DatabaseConnection.getInstance().close();

            System.out.println();
            System.out.println("=== Demo finalizat. Verifica audit.csv ===");

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