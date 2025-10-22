package tests;

import config.coneccion;
import java.sql.*;

public class seguridad {

    public static void main(String[] args) {
        crearTablas();
        crearUsuarioLimitado();
        crearVistas();
        pruebasIntegridad();
        consultaSegura();
    }

    // 🏗️ Crear base y tablas si no existen
    private static void crearTablas() {
        try (Connection conn = coneccion.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS basedatos_tpintegrador");
            stmt.executeUpdate("USE basedatos_tpintegrador");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Producto(
                    id_producto INT PRIMARY KEY AUTO_INCREMENT,
                    nombre VARCHAR(50) NOT NULL,
                    descripcion VARCHAR(60),
                    precio FLOAT CHECK (precio >= 0),
                    stock INT CHECK (stock >= 0)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS CodigoBarras(
                    id_codigoBarra INT PRIMARY KEY AUTO_INCREMENT,
                    valor CHAR(13) UNIQUE NOT NULL,
                    fechaCreacion VARCHAR(10),
                    id_producto INT NOT NULL,
                    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
                )
            """);

            stmt.executeUpdate("""
                INSERT INTO Producto (nombre_producto, descripcion_producto, precio, stock)
                VALUES ('Mate Imperial', 'Calabaza forrada', 19999.99, 10),
                       ('Termo Acero', 'Acero inoxidable 1L', 34999.00, 25)
                ON DUPLICATE KEY UPDATE nombre_producto = nombre_producto
            """);

            System.out.println("✅ Base de datos y tablas listas.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 👤 Crear usuario con privilegios mínimos
    private static void crearUsuarioLimitado() {
        try (Connection conn = coneccion.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE USER IF NOT EXISTS 'app_user'@'localhost' IDENTIFIED BY 'Secure123!'");
            stmt.executeUpdate("GRANT SELECT, INSERT, UPDATE ON basedatos_tpintegrador.* TO 'app_user'@'localhost'");
            stmt.executeUpdate("FLUSH PRIVILEGES");

            System.out.println("Usuario 'app_user' creado con privilegios mínimos.\n");
        } catch (SQLException e) {
            System.out.println("️ Advertencia (puede ya existir el usuario): " + e.getMessage());
        }
    }

    // Crear vistas que oculten información sensible
    private static void crearVistas() {
        try (Connection conn = coneccion.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE OR REPLACE VIEW vw_producto_publico AS
                SELECT id_producto, nombre_producto, descripcion_producto, stock
                FROM Producto
            """);

            stmt.executeUpdate("""
                CREATE OR REPLACE VIEW vw_codigos_publicos AS
                SELECT id_codigoBarra, valor, id_producto
                FROM CodigoBarras
            """);

            System.out.println("✅ Vistas creadas correctamente.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Pruebas de integridad
    private static void pruebasIntegridad() {
        System.out.println("🧩 Pruebas de integridad:");

        // PK duplicada
        try (Connection conn = coneccion.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 INSERT INTO Producto (id_producto, nombre_producto, descripcion_producto, precio, stock)
                 VALUES (?, ?, ?, ?, ?)
             """)) {

            ps.setInt(1, 1);
            ps.setString(2, "Duplicado");
            ps.setString(3, "PK repetida");
            ps.setFloat(4, 1000);
            ps.setInt(5, 10);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error esperado (PK duplicada): " + e.getMessage());
        }

        // FK inválida
        try (Connection conn = coneccion.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 INSERT INTO CodigoBarras (valor, fechaCreacion, id_producto)
                 VALUES (?, ?, ?)
             """)) {

            ps.setString(1, "9999999999999");
            ps.setString(2, "2025-10-21");
            ps.setInt(3, 9999); // producto que no existe
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error esperado (FK inválida): " + e.getMessage());
        }

        // CHECK inválido
        try (Connection conn = coneccion.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 INSERT INTO Producto (nombre_producto, descripcion_producto, precio, stock)
                 VALUES (?, ?, ?, ?)
             """)) {

            ps.setString(1, "ProductoNegativo");
            ps.setString(2, "Precio fuera de rango");
            ps.setFloat(3, -50); // precio inválido
            ps.setInt(4, 10);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error esperado (CHECK inválido): " + e.getMessage());
        }

        System.out.println("Pruebas de integridad finalizadas.\n");
    }

    //Consulta segura con PreparedStatement
    private static void consultaSegura() {
        System.out.println("Consulta segura (sin SQL Injection):");
        try (Connection conn = coneccion.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 SELECT id_producto, nombre_producto, stock
                 FROM vw_producto_publico
                 WHERE nombre_producto = ?
             """)) {

            // Parametrizado: evita inyección
            ps.setString(1, "Mate Imperial");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.printf("➡️ ID: %d | Nombre: %s | Stock: %d%n",
                        rs.getInt("id_producto"),
                        rs.getString("nombre_producto"),
                        rs.getInt("stock"));
            }

            System.out.println("\nConsulta segura ejecutada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
