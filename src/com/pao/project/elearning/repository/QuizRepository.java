package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Quiz;
import com.pao.project.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizRepository implements Repository<Quiz, Integer> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Quiz mapRow(ResultSet rs) throws SQLException {
        return new Quiz(
                rs.getInt("id"),
                rs.getString("titlu")
        );
    }

    @Override
    public void save(Quiz quiz) throws SQLException {
        String cursCod = findFirstCourseCode();

        if (cursCod == null) {
            throw new SQLException("Nu exista niciun curs pentru asocierea quiz-ului.");
        }

        saveForCourse(quiz, cursCod);
    }

    public void saveForCourse(Quiz quiz, String cursCod) throws SQLException {
        String sql = "INSERT INTO quiz (id, titlu, curs_cod, numar_rezultate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, quiz.getId());
            ps.setString(2, quiz.getTitlu());
            ps.setString(3, cursCod);
            ps.setInt(4, 0);

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Quiz> findById(Integer id) throws SQLException {
        String sql = "SELECT id, titlu FROM quiz WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Quiz> findAll() throws SQLException {
        String sql = "SELECT id, titlu FROM quiz ORDER BY id";
        List<Quiz> quizuri = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                quizuri.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return quizuri;
    }

    @Override
    public void update(Quiz quiz) throws SQLException {
        String sql = "UPDATE quiz SET titlu = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, quiz.getTitlu());
            ps.setInt(2, quiz.getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM quiz WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    private String findFirstCourseCode() throws SQLException {
        String sql = "SELECT cod FROM curs ORDER BY cod LIMIT 1";

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("cod");
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return null;
    }
}