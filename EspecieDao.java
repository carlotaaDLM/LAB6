package com.example.lab7_iweb.Daos;

import com.example.lab7_iweb.Beans.Especie;

import java.sql.*;
import java.util.ArrayList;

public class EspecieDao extends DaoBase<Especie> {

    /** Usado para llenar los ComboBox de especie. */
    public ArrayList<Especie> listar() {
        ArrayList<Especie> lista = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT idespecie, nombre FROM especie ORDER BY nombre")) {
            while (rs.next()) {
                Especie e = new Especie();
                e.setIdEspecie(rs.getInt(1));
                e.setNombre(rs.getString(2));
                lista.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    @Override
    public void crear(Especie especie) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO especie (nombre) VALUES (?)")) {
            pstmt.setString(1, especie.getNombre());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void borrar(int idEspecie) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM especie WHERE idespecie = ?")) {
            pstmt.setInt(1, idEspecie);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
