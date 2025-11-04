/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entities.CodigoDeBarras;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CodigoDeBarrasDaoJdbc implements GenericDao<CodigoDeBarras> {

    @Override
    public CodigoDeBarras crear(CodigoDeBarras codigo, Connection conn) throws SQLException {
        String sql = "INSERT INTO codigobarras (codigo, fecha_creacion, id_producto, borrado) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, codigo.getCodigo());

            // ZonedDateTime -> Timestamp
            Timestamp ts = Timestamp.from(codigo.getFechaCreacion().toInstant());
            stmt.setTimestamp(2, ts);

            stmt.setInt(3, codigo.getProducto().getIdProducto());
            stmt.setBoolean(4, codigo.isBorrado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // Si querés setear el id en la entidad, agregá un setter en tu clase CódigoDeBarras
                    // codigo.setIdCodigoDeBarras(rs.getInt(1));
                }
            }
        }
        return codigo;
    }

    @Override
    public Optional<CodigoDeBarras> leerPorId(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM codigobarras WHERE id_codigobarras = ? AND borrado = FALSE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CodigoDeBarras codigo = new CodigoDeBarras(
                    rs.getInt("id_codigobarras"),
                    rs.getString("codigo"),
                    null // Producto se carga después en el Service si hace falta
                );

                return Optional.of(codigo);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<CodigoDeBarras> leerTodos(Connection conn) throws SQLException {
        String sql = "SELECT * FROM codigobarras WHERE borrado = FALSE";
        List<CodigoDeBarras> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CodigoDeBarras codigo = new CodigoDeBarras(
                    rs.getInt("id_codigobarras"),
                    rs.getString("codigo"),
                    null // Producto va fuera del DAO
                );
                lista.add(codigo);
            }
        }
        return lista;
    }

    @Override
    public boolean actualizar(CodigoDeBarras codigo, Connection conn) throws SQLException {
        String sql = "UPDATE codigobarras SET codigo = ?, borrado = ? WHERE id_codigobarras = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo.getCodigo());
            stmt.setBoolean(2, codigo.isBorrado());
            stmt.setInt(3, codigo.getCodigoDeBarrasId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id, Connection conn) throws SQLException {
        String sql = "UPDATE codigobarras SET borrado = TRUE WHERE id_codigobarras = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public Optional<CodigoDeBarras> leerPorProductoId(int productoId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM codigobarras WHERE id_producto = ? AND borrado = FALSE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CodigoDeBarras codigo = new CodigoDeBarras(
                        rs.getInt("id_codigobarras"),
                        rs.getString("codigo"),
                        null
                );
                return Optional.of(codigo);
            }
        }
        return Optional.empty();
    }
}
