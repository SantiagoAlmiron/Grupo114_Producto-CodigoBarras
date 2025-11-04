package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author santiago
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    T crear(T t, Connection conn) throws SQLException;
    Optional<T> leerPorId(int id, Connection conn) throws SQLException;
    List<T> leerTodos(Connection conn) throws SQLException;
    boolean actualizar(T t, Connection conn) throws SQLException;
    boolean eliminar(int id, Connection conn) throws SQLException;
}

