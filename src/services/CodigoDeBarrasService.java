/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import dao.CodigoDeBarrasDaoJdbc;
import entities.CodigoDeBarras;
import entities.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de manejar los códigos de barras de los productos.
 * Persiste los datos utilizando DAO y trabaja dentro de transacciones externas.
 * 
 * @author Santiago
 */
public class CodigoDeBarrasService {

    private final CodigoDeBarrasDaoJdbc codigoDao = new CodigoDeBarrasDaoJdbc();

    /**
     * Crea un código de barras para un producto dentro de una transacción.
     */
    public CodigoDeBarras crear(String valor, Producto producto, Connection conn) throws SQLException {
        // Creamos objeto en memoria
        CodigoDeBarras codigo = new CodigoDeBarras(0, valor, producto);

        // Lo persistimos en BD
        codigoDao.crear(codigo, conn);

        // Vinculación bidireccional segura
        producto.setCodigoDeBarras(codigo);

        return codigo;
    }

    /**
     * Baja lógica de código por ID de producto (relación 1:1)
     */
    public boolean eliminarPorProductoId(int idProducto, Connection conn) throws SQLException {
        String sql = "UPDATE codigobarras SET borrado = TRUE WHERE id_producto = ?";

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene código por ID de código
     */
    public Optional<CodigoDeBarras> getById(int id, Connection conn) throws SQLException {
        return codigoDao.leerPorId(id, conn);
    }

    /**
     * Obtiene todos los códigos no borrados
     */
    public List<CodigoDeBarras> getAll(Connection conn) throws SQLException {
        return codigoDao.leerTodos(conn);
    }
}
