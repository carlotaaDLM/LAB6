package com.example.lab7_iweb.Daos;

import com.example.lab7_iweb.Beans.Veterinario;

import java.sql.*;
import java.util.ArrayList;

public class VeterinarioDao extends DaoBase<Veterinario> {

    /** Usado para llenar los ComboBox de veterinario. */
    public ArrayList<Veterinario> listar() {
        ArrayList<Veterinario> lista = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT idveterinario, nombre, especialidad FROM veterinario ORDER BY nombre")) {
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setIdVeterinario(rs.getInt(1));
                v.setNombre(rs.getString(2));
                v.setEspecialidad(rs.getString(3));
                lista.add(v);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    @Override
    public void crear(Veterinario veterinario) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO veterinario (nombre, especialidad) VALUES (?, ?)")) {
            pstmt.setString(1, veterinario.getNombre());
            pstmt.setString(2, veterinario.getEspecialidad());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void borrar(int idVeterinario) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM veterinario WHERE idveterinario = ?")) {
            pstmt.setInt(1, idVeterinario);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
