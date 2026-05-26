package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstructorRepository implements Repository<Instructor, Integer> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Instructor mapRow(ResultSet rs) throws SQLException {
        return new Instructor(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("specializare")
        );
    }

    @Override
    public void save(Instructor instructor) throws SQLException {
        String sql = "INSERT INTO instructor (id, nume, email, username, specializare) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, instructor.getId());
            ps.setString(2, instructor.getNume());
            ps.setString(3, instructor.getEmail());
            ps.setString(4, instructor.getUsername());
            ps.setString(5, instructor.getSpecializare());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Instructor> findById(Integer id) throws SQLException {
        String sql = "SELECT id, nume, email, username, specializare FROM instructor WHERE id = ?";

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
    public List<Instructor> findAll() throws SQLException {
        String sql = "SELECT id, nume, email, username, specializare FROM instructor ORDER BY id";
        List<Instructor> instructori = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                instructori.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return instructori;
    }

    @Override
    public void update(Instructor instructor) throws SQLException {
        String sql = "UPDATE instructor SET nume = ?, email = ?, username = ?, specializare = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, instructor.getNume());
            ps.setString(2, instructor.getEmail());
            ps.setString(3, instructor.getUsername());
            ps.setString(4, instructor.getSpecializare());
            ps.setInt(5, instructor.getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM instructor WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}