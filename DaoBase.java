package com.example.lab7_iweb.Daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Pregunta 1 - DaoBase
 * Clase abstracta que centraliza la configuración JDBC.
 * Todos los DAOs heredan de ella para reutilizar getConnection().
 *
 * Es genérica (<T>) para que crear() y borrar() sirvan para "cualquier entidad":
 * MascotaDao extends DaoBase<Mascota>, EspecieDao extends DaoBase<Especie>, etc.
 */
public abstract class DaoBase<T> {

    private static final String URL = "jdbc:mysql://localhost:3306/Veterinaria";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Inserta una entidad en la BD. Cada DAO hijo debe implementarlo. */
    public abstract void crear(T entidad);

    /** Borra una entidad por su ID. Cada DAO hijo debe implementarlo. */
    public abstract void borrar(int id);
}
