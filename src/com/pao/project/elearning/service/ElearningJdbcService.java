package com.pao.project.elearning.service;

import com.pao.project.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ElearningJdbcService {
    private static ElearningJdbcService instance;

    private ElearningJdbcService() {
    }

    public static ElearningJdbcService getInstance() {
        if (instance == null) {
            instance = new ElearningJdbcService();
        }

        return instance;
    }

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public void adaugaRezultatCuTranzactie(int cursantId, int quizId, int scor) throws SQLException, IOException {
        Connection connection = getConn();

        connection.setAutoCommit(false);

        try {
            String checkCursantSql = "SELECT id FROM cursant WHERE id = ?";

            try (PreparedStatement ps = connection.prepareStatement(checkCursantSql)) {
                ps.setInt(1, cursantId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Cursantul cu id-ul " + cursantId + " nu exista.");
                    }
                }
            }

            String checkQuizSql = "SELECT id FROM quiz WHERE id = ?";

            try (PreparedStatement ps = connection.prepareStatement(checkQuizSql)) {
                ps.setInt(1, quizId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Quiz-ul cu id-ul " + quizId + " nu exista.");
                    }
                }
            }

            String insertSql = """
                    INSERT INTO rezultat_quiz (cursant_id, quiz_id, scor, data_rezultat)
                    VALUES (?, ?, ?, ?)
                    """;

            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setInt(1, cursantId);
                ps.setInt(2, quizId);
                ps.setInt(3, scor);
                ps.setString(4, LocalDate.now().toString());

                ps.executeUpdate();
            }

            String updateSql = "UPDATE quiz SET numar_rezultate = numar_rezultate + 1 WHERE id = ?";

            try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                ps.setInt(1, quizId);
                ps.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;

        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<String> getCursuriCuInstructori() throws SQLException, IOException {
        String sql = """
                SELECT c.cod AS curs_cod,
                       c.titlu AS curs_titlu,
                       c.durata_ore AS durata_ore,
                       i.nume AS instructor_nume,
                       i.specializare AS specializare
                FROM curs c
                JOIN instructor i ON c.instructor_id = i.id
                ORDER BY c.titlu
                """;

        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rezultate.add(rs.getString("curs_cod") + " - "
                        + rs.getString("curs_titlu") + " | instructor: "
                        + rs.getString("instructor_nume") + " | "
                        + rs.getString("specializare") + " | "
                        + rs.getInt("durata_ore") + " ore");
            }
        }

        return rezultate;
    }

    public List<String> getQuizuriCuCursuri() throws SQLException, IOException {
        String sql = """
                SELECT q.id AS quiz_id,
                       q.titlu AS quiz_titlu,
                       c.cod AS curs_cod,
                       c.titlu AS curs_titlu
                FROM quiz q
                JOIN curs c ON q.curs_cod = c.cod
                ORDER BY c.cod, q.id
                """;

        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rezultate.add("Quiz #" + rs.getInt("quiz_id") + " - "
                        + rs.getString("quiz_titlu") + " | curs: "
                        + rs.getString("curs_cod") + " - "
                        + rs.getString("curs_titlu"));
            }
        }

        return rezultate;
    }

    public List<String> getRezultateCuDetalii() throws SQLException, IOException {
        String sql = """
                SELECT rq.id AS rezultat_id,
                       cr.nume AS cursant_nume,
                       q.titlu AS quiz_titlu,
                       c.titlu AS curs_titlu,
                       rq.scor AS scor,
                       rq.data_rezultat AS data_rezultat
                FROM rezultat_quiz rq
                JOIN cursant cr ON rq.cursant_id = cr.id
                JOIN quiz q ON rq.quiz_id = q.id
                JOIN curs c ON q.curs_cod = c.cod
                ORDER BY rq.scor DESC
                """;

        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rezultate.add("Rezultat #" + rs.getInt("rezultat_id")
                        + " | " + rs.getString("cursant_nume")
                        + " | quiz: " + rs.getString("quiz_titlu")
                        + " | curs: " + rs.getString("curs_titlu")
                        + " | scor: " + rs.getInt("scor")
                        + " | data: " + rs.getString("data_rezultat"));
            }
        }

        return rezultate;
    }

    public List<String> getMedieScorPeCurs() throws SQLException, IOException {
        String sql = """
                SELECT c.cod AS curs_cod,
                       c.titlu AS curs_titlu,
                       AVG(rq.scor) AS medie_scor,
                       COUNT(rq.id) AS numar_rezultate
                FROM curs c
                LEFT JOIN quiz q ON q.curs_cod = c.cod
                LEFT JOIN rezultat_quiz rq ON rq.quiz_id = q.id
                GROUP BY c.cod, c.titlu
                ORDER BY medie_scor DESC
                """;

        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double medie = rs.getDouble("medie_scor");

                rezultate.add(rs.getString("curs_cod") + " - "
                        + rs.getString("curs_titlu") + " | medie scor: "
                        + medie + " | rezultate: "
                        + rs.getInt("numar_rezultate"));
            }
        }

        return rezultate;
    }
}