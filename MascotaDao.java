package com.example.lab7_iweb.Daos;

import com.example.lab7_iweb.Beans.Dueno;
import com.example.lab7_iweb.Beans.Especie;
import com.example.lab7_iweb.Beans.Mascota;
import com.example.lab7_iweb.Beans.Veterinario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MascotaDao extends DaoBase<Mascota> {

    private static final String SQL_BASE =
            "SELECT m.idmascota, m.nombre, m.edad, m.peso, " +
            "       e.idespecie, e.nombre AS especie, " +
            "       v.idveterinario, v.nombre AS veterinario, v.especialidad, " +
            "       d.iddueno, d.nombre AS dueno, d.telefono " +
            "FROM mascota m " +
            "INNER JOIN especie e     ON m.especie_id = e.idespecie " +
            "INNER JOIN veterinario v ON m.veterinario_id = v.idveterinario " +
            "INNER JOIN dueno d       ON m.dueno_id = d.iddueno ";

    // ===================== Pregunta 2: listar =====================
    public ArrayList<Mascota> listar() {
        return listar(0, 0);
    }

    // ===================== Pregunta 4: listar con filtro =====================
    /**
     * Lista mascotas filtrando por especie y/o veterinario.
     * Un valor 0 significa "sin filtro" (opción "Todas/Todos" del ComboBox).
     */
    public ArrayList<Mascota> listar(int especieId, int veterinarioId) {
        ArrayList<Mascota> lista = new ArrayList<>();

        String sql = SQL_BASE + "WHERE 1 = 1 ";
        if (especieId > 0) {
            sql += "AND m.especie_id = ? ";
        }
        if (veterinarioId > 0) {
            sql += "AND m.veterinario_id = ? ";
        }
        sql += "ORDER BY m.idmascota";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int i = 1;
            if (especieId > 0) {
                pstmt.setInt(i++, especieId);
            }
            if (veterinarioId > 0) {
                pstmt.setInt(i, veterinarioId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(fetchMascota(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Mascota obtener(int idMascota) {
        Mascota mascota = null;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_BASE + "WHERE m.idmascota = ?")) {
            pstmt.setInt(1, idMascota);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    mascota = fetchMascota(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mascota;
    }

    // ===================== Pregunta 3: crear (método abstracto heredado) =====================
    @Override
    public void crear(Mascota mascota) {
        String sql = "INSERT INTO mascota (nombre, edad, peso, especie_id, veterinario_id, dueno_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mascota.getNombre());
            pstmt.setInt(2, mascota.getEdad());
            pstmt.setBigDecimal(3, mascota.getPeso());
            pstmt.setInt(4, mascota.getEspecie().getIdEspecie());
            pstmt.setInt(5, mascota.getVeterinario().getIdVeterinario());
            pstmt.setInt(6, mascota.getDueno().getIdDueno());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===================== Pregunta 3: borrar (método abstracto heredado) =====================
    @Override
    public void borrar(int idMascota) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM mascota WHERE idmascota = ?")) {
            pstmt.setInt(1, idMascota);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Arma la Mascota con sus Beans internos (Especie, Veterinario, Dueno). */
    private Mascota fetchMascota(ResultSet rs) throws SQLException {
        Mascota m = new Mascota();
        m.setIdMascota(rs.getInt("idmascota"));
        m.setNombre(rs.getString("nombre"));
        m.setEdad(rs.getInt("edad"));
        m.setPeso(rs.getBigDecimal("peso"));

        Especie especie = new Especie();
        especie.setIdEspecie(rs.getInt("idespecie"));
        especie.setNombre(rs.getString("especie"));
        m.setEspecie(especie);

        Veterinario veterinario = new Veterinario();
        veterinario.setIdVeterinario(rs.getInt("idveterinario"));
        veterinario.setNombre(rs.getString("veterinario"));
        veterinario.setEspecialidad(rs.getString("especialidad"));
        m.setVeterinario(veterinario);

        Dueno dueno = new Dueno();
        dueno.setIdDueno(rs.getInt("iddueno"));
        dueno.setNombre(rs.getString("dueno"));
        dueno.setTelefono(rs.getString("telefono"));
        m.setDueno(dueno);

        return m;
    }
}
