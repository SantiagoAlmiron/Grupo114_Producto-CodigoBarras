/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author santiago
 */

import entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import entities.CodigoDeBarras;

public class ProductoDaoJdbc implements GenericDao<Producto> {

    private static final String TABLA = "producto";

    @Override
    public Producto crear(Producto p, Connection conn) throws SQLException {
        String sql = "INSERT INTO " + TABLA + " (nombre, descripcion, stock, precio, borrado) VALUES (?, ?, ?, ?, FALSE)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getDescripcion());
            stmt.setInt(3, p.getStock());
            stmt.setDouble(4, p.getPrecio());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                p.setIdProducto(rs.getInt(1));
            }
        }
        return p;
    }

    @Override
    public Optional<Producto> leerPorId(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM " + TABLA + " WHERE id_producto = ? AND borrado = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Producto> leerTodos(Connection conn) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLA + " WHERE borrado = FALSE LIMIT 10";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(mapear(rs));
            }
        }
        return productos;
    }

    @Override
    public boolean actualizar(Producto p, Connection conn) throws SQLException {
        String sql = "UPDATE " + TABLA + " SET nombre = ?, precio = ? WHERE id_producto = ? AND borrado = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setInt(3, p.getIdProducto());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id, Connection conn) throws SQLException {
        String sql = "UPDATE " + TABLA + " SET borrado = TRUE WHERE id_producto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // 🔸 Método auxiliar
    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto(
            rs.getInt("id_producto"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getInt("stock"),
            rs.getDouble("precio")
        );
        p.setBorrado(rs.getBoolean("borrado"));
        return p;
    }
    
    public Optional<Producto> leerPorIdConCodigo(int id, Connection conn) throws SQLException {
        Optional<Producto> optProd = leerPorId(id, conn);
        if (optProd.isEmpty()) return Optional.empty();

        Producto p = optProd.get();

        CodigoDeBarrasDaoJdbc codigoDao = new CodigoDeBarrasDaoJdbc();
        Optional<CodigoDeBarras> optCodigo = codigoDao.leerPorProductoId(id, conn);

        if (optCodigo.isPresent()) {
            CodigoDeBarras codigo = optCodigo.get();
            p.setCodigoDeBarras(codigo);
        }

        return Optional.of(p);
    }
}
