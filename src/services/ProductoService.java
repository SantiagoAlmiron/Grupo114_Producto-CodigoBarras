/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author santiago
 */
import config.Conexion;
import entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoService {

    // ✅ Nombre de la tabla como constante
    private static final String TABLA = "producto";

    // ✅ Crear producto
    public Producto crear(String nombre, String descripcion, int stock, double precio) {
        String sql = "INSERT INTO " + TABLA + " (nombre, descripcion, stock, precio, borrado) VALUES (?, ?, ?, ?, FALSE)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, stock);
            stmt.setDouble(4, precio);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    return new Producto(idGenerado, nombre, descripcion, stock, precio);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al crear el producto:");
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Obtener todos los productos (máximo 10)
    public List<Producto> getAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLA + " WHERE borrado = FALSE LIMIT 10";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precio")
                );
                p.setBorrado(rs.getBoolean("borrado"));
                productos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener productos:");
            e.printStackTrace();
        }

        return productos;
    }

    // ✅ Buscar producto por ID
    public Optional<Producto> getById(int id) {
        String sql = "SELECT * FROM " + TABLA + " WHERE id_producto = ? AND borrado = FALSE";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precio")
                );
                p.setBorrado(rs.getBoolean("borrado"));
                return Optional.of(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar producto por ID:");
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // ✅ Actualizar producto
    public boolean actualizar(int id, String nuevoNombre, double nuevoPrecio) {
        String sql = "UPDATE " + TABLA + " SET nombre = ?, precio = ? WHERE id_producto = ? AND borrado = FALSE";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoNombre);
            stmt.setDouble(2, nuevoPrecio);
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar producto:");
            e.printStackTrace();
        }

        return false;
    }

    // ✅ Marcar producto como borrado (soft delete)
    public boolean eliminar(int id) {
        String sql = "UPDATE " + TABLA + " SET borrado = TRUE WHERE id_producto = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto:");
            e.printStackTrace();
        }

        return false;
    }
}
