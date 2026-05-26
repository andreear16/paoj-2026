package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Cursant;
import com.pao.project.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursantRepository implements Repository<Cursant, Integer> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Cursant mapRow(ResultSet rs) throws SQLException {
        return new Cursant(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getInt("an_studiu")
        );
    }

    @Override
    public void save(Cursant cursant) throws SQLException {
        String sql = "INSERT INTO cursant (id, nume, email, username, an_studiu) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, cursant.getId());
            ps.setString(2, cursant.getNume());
            ps.setString(3, cursant.getEmail());
            ps.setString(4, cursant.getUsername());
            ps.setInt(5, cursant.getAnStudiu());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Cursant> findById(Integer id) throws SQLException {
        String sql = "SELECT id, nume, email, username, an_studiu FROM cursant WHERE id = ?";

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
    public List<Cursant> findAll() throws SQLException {
        String sql = "SELECT id, nume, email, username, an_studiu FROM cursant ORDER BY id";
        List<Cursant> cursanti = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cursanti.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return cursanti;
    }

    @Override
    public void update(Cursant cursant) throws SQLException {
        String sql = "UPDATE cursant SET nume = ?, email = ?, username = ?, an_studiu = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cursant.getNume());
            ps.setString(2, cursant.getEmail());
            ps.setString(3, cursant.getUsername());
            ps.setInt(4, cursant.getAnStudiu());
            ps.setInt(5, cursant.getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM cursant WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}