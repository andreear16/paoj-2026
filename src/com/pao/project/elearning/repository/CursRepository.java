package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.CodCurs;
import com.pao.project.elearning.model.Curs;
import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursRepository implements Repository<Curs, String> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Curs mapRow(ResultSet rs) throws SQLException {
        Instructor instructor = new Instructor(
                rs.getInt("instructor_id"),
                rs.getString("instructor_nume"),
                rs.getString("instructor_email"),
                rs.getString("instructor_username"),
                rs.getString("instructor_specializare")
        );

        return new Curs(
                new CodCurs(rs.getString("cod")),
                rs.getString("titlu"),
                instructor,
                rs.getInt("durata_ore")
        );
    }

    @Override
    public void save(Curs curs) throws SQLException {
        String sql = "INSERT INTO curs (cod, titlu, instructor_id, durata_ore) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, curs.getCod().getCod());
            ps.setString(2, curs.getTitlu());
            ps.setInt(3, curs.getInstructor().getId());
            ps.setInt(4, curs.getDurataOre());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Curs> findById(String cod) throws SQLException {
        String sql = """
                SELECT c.cod,
                       c.titlu,
                       c.durata_ore,
                       i.id AS instructor_id,
                       i.nume AS instructor_nume,
                       i.email AS instructor_email,
                       i.username AS instructor_username,
                       i.specializare AS instructor_specializare
                FROM curs c
                JOIN instructor i ON c.instructor_id = i.id
                WHERE c.cod = ?
                """;

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cod);

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
    public List<Curs> findAll() throws SQLException {
        String sql = """
                SELECT c.cod,
                       c.titlu,
                       c.durata_ore,
                       i.id AS instructor_id,
                       i.nume AS instructor_nume,
                       i.email AS instructor_email,
                       i.username AS instructor_username,
                       i.specializare AS instructor_specializare
                FROM curs c
                JOIN instructor i ON c.instructor_id = i.id
                ORDER BY c.titlu
                """;

        List<Curs> cursuri = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cursuri.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return cursuri;
    }

    @Override
    public void update(Curs curs) throws SQLException {
        String sql = "UPDATE curs SET titlu = ?, instructor_id = ?, durata_ore = ? WHERE cod = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, curs.getTitlu());
            ps.setInt(2, curs.getInstructor().getId());
            ps.setInt(3, curs.getDurataOre());
            ps.setString(4, curs.getCod().getCod());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(String cod) throws SQLException {
        String sql = "DELETE FROM curs WHERE cod = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cod);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}