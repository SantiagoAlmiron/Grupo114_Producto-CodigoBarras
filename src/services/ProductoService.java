/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import config.Conexion;
import dao.ProductoDaoJdbc;
import entities.Producto;
import entities.CodigoDeBarras;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoService {

    private final ProductoDaoJdbc productoDao = new ProductoDaoJdbc();
    private final CodigoDeBarrasService codigoService = new CodigoDeBarrasService();

    // ✅ Crear producto + código de barras
    public Producto crear(String nombre, String descripcion, int stock, double precio) {
        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false); // Inicio transacción

            // 1) Crear producto
            Producto nuevo = new Producto(nombre, descripcion, stock, precio);
            productoDao.crear(nuevo, conn); // Ahora nuevo tiene ID

            // 2) Crear código de barras usando el ID real
            String valorCodigo = "PROD-" + nuevo.getIdProducto() + "-" + System.currentTimeMillis();
            codigoService.crear(valorCodigo, nuevo, conn); // Asigna en memoria también

            // ✅ Transacción OK
            conn.commit();
            System.out.println("✅ Producto y código de barras creados correctamente. ID Producto: " + nuevo.getIdProducto());
            return nuevo;

        } catch (SQLException e) {
            System.err.println("❌ Error al crear producto + código (rollback): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Obtener todos los productos
    public List<Producto> getAll() {
        try (Connection conn = Conexion.getConnection()) {
            return productoDao.leerTodos(conn);
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // ✅ Buscar producto por ID
    public Optional<Producto> getById(int id) {
        try (Connection conn = Conexion.getConnection()) {
            return productoDao.leerPorIdConCodigo(id, conn);
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar producto por ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // ✅ Actualizar producto
    public boolean actualizar(int id, String nuevoNombre, double nuevoPrecio) {
        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);

            Optional<Producto> opt = productoDao.leerPorId(id, conn);
            if (opt.isEmpty()) {
                System.err.println("⚠️ Producto no encontrado ID: " + id);
                return false;
            }

            Producto p = opt.get();
            p.setNombre(nuevoNombre);
            p.setPrecio(nuevoPrecio);

            boolean actualizado = productoDao.actualizar(p, conn);
            conn.commit();

            if (actualizado) {
                System.out.println("✅ Producto actualizado correctamente.");
            } else {
                System.out.println("⚠️ No se pudo actualizar el producto.");
            }

            return actualizado;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Baja lógica de producto + código de barras
    public boolean eliminar(int id) {
        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);

            // 1) Baja lógica código de barras
            codigoService.eliminarPorProductoId(id, conn);

            // 2) Baja lógica producto
            boolean eliminado = productoDao.eliminar(id, conn);

            conn.commit();

            if (eliminado) {
                System.out.println("✅ Producto y su código de barras eliminados lógicamente.");
            } else {
                System.out.println("⚠️ No se encontró el producto para eliminar.");
            }

            return eliminado;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto + código: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
