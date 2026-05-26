package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.CodCurs;
import com.pao.project.elearning.model.Curs;
import com.pao.project.elearning.model.Cursant;
import com.pao.project.elearning.model.Inscriere;
import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InscriereRepository implements Repository<Inscriere, Integer> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Inscriere mapRow(ResultSet rs) throws SQLException {
        Cursant cursant = new Cursant(
                rs.getInt("cursant_id"),
                rs.getString("cursant_nume"),
                rs.getString("cursant_email"),
                rs.getString("cursant_username"),
                rs.getInt("cursant_an_studiu")
        );

        Instructor instructor = new Instructor(
                rs.getInt("instructor_id"),
                rs.getString("instructor_nume"),
                rs.getString("instructor_email"),
                rs.getString("instructor_username"),
                rs.getString("instructor_specializare")
        );

        Curs curs = new Curs(
                new CodCurs(rs.getString("curs_cod")),
                rs.getString("curs_titlu"),
                instructor,
                rs.getInt("curs_durata_ore")
        );

        return new Inscriere(
                rs.getInt("id"),
                cursant,
                curs,
                rs.getString("data_inscriere")
        );
    }

    @Override
    public void save(Inscriere inscriere) throws SQLException {
        String sql = "INSERT INTO inscriere (id, cursant_id, curs_cod, data_inscriere) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, inscriere.getId());
            ps.setInt(2, inscriere.getCursant().getId());
            ps.setString(3, inscriere.getCurs().getCod().getCod());
            ps.setString(4, inscriere.getDataInscriere());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Inscriere> findById(Integer id) throws SQLException {
        String sql = """
                SELECT ins.id,
                       ins.data_inscriere,
                       crs.id AS cursant_id,
                       crs.nume AS cursant_nume,
                       crs.email AS cursant_email,
                       crs.username AS cursant_username,
                       crs.an_studiu AS cursant_an_studiu,
                       c.cod AS curs_cod,
                       c.titlu AS curs_titlu,
                       c.durata_ore AS curs_durata_ore,
                       i.id AS instructor_id,
                       i.nume AS instructor_nume,
                       i.email AS instructor_email,
                       i.username AS instructor_username,
                       i.specializare AS instructor_specializare
                FROM inscriere ins
                JOIN cursant crs ON ins.cursant_id = crs.id
                JOIN curs c ON ins.curs_cod = c.cod
                JOIN instructor i ON c.instructor_id = i.id
                WHERE ins.id = ?
                """;

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
    public List<Inscriere> findAll() throws SQLException {
        String sql = """
                SELECT ins.id,
                       ins.data_inscriere,
                       crs.id AS cursant_id,
                       crs.nume AS cursant_nume,
                       crs.email AS cursant_email,
                       crs.username AS cursant_username,
                       crs.an_studiu AS cursant_an_studiu,
                       c.cod AS curs_cod,
                       c.titlu AS curs_titlu,
                       c.durata_ore AS curs_durata_ore,
                       i.id AS instructor_id,
                       i.nume AS instructor_nume,
                       i.email AS instructor_email,
                       i.username AS instructor_username,
                       i.specializare AS instructor_specializare
                FROM inscriere ins
                JOIN cursant crs ON ins.cursant_id = crs.id
                JOIN curs c ON ins.curs_cod = c.cod
                JOIN instructor i ON c.instructor_id = i.id
                ORDER BY ins.id
                """;

        List<Inscriere> inscrieri = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                inscrieri.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return inscrieri;
    }

    @Override
    public void update(Inscriere inscriere) throws SQLException {
        String sql = "UPDATE inscriere SET cursant_id = ?, curs_cod = ?, data_inscriere = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, inscriere.getCursant().getId());
            ps.setString(2, inscriere.getCurs().getCod().getCod());
            ps.setString(3, inscriere.getDataInscriere());
            ps.setInt(4, inscriere.getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM inscriere WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}