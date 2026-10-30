package com.example.lab7_iweb.Daos;

import com.example.lab7_iweb.Beans.Dueno;

import java.sql.*;
import java.util.ArrayList;

public class DuenoDao extends DaoBase<Dueno> {

    /** Usado para llenar el ComboBox de dueño. */
    public ArrayList<Dueno> listar() {
        ArrayList<Dueno> lista = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT iddueno, nombre, telefono FROM dueno ORDER BY nombre")) {
            while (rs.next()) {
                Dueno d = new Dueno();
                d.setIdDueno(rs.getInt(1));
                d.setNombre(rs.getString(2));
                d.setTelefono(rs.getString(3));
                lista.add(d);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    @Override
    public void crear(Dueno dueno) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO dueno (nombre, telefono) VALUES (?, ?)")) {
            pstmt.setString(1, dueno.getNombre());
            pstmt.setString(2, dueno.getTelefono());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void borrar(int idDueno) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM dueno WHERE iddueno = ?")) {
            pstmt.setInt(1, idDueno);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
