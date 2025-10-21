package tests;

import config.coneccion;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class concurrencia {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASS");

        // Paso 1: Crear la base de datos y tabla si no existen
        try (Connection conn = coneccion.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS basedatos_tpintegrador");
            stmt.executeUpdate("USE basedatos_tpintegrador");
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Producto (" +
                            "id_producto INT PRIMARY KEY AUTO_INCREMENT," +
                            "nombre VARCHAR(50) NOT NULL," +
                            "descripcion VARCHAR(60)," +
                            "precio FLOAT CHECK (precio >= 0)," +
                            "stock INT CHECK (stock >= 0)" +
                            ")"
            );

            // Crear tabla CodigoBarras
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS CodigoBarras (" +
                            "id_codigoBarra INT PRIMARY KEY AUTO_INCREMENT," +
                            "valor CHAR(13) UNIQUE NOT NULL," +
                            "fechaCreacion VARCHAR(10)," +
                            "id_producto INT NOT NULL," +
                            "FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)" +
                            ")"
            );

            // Insertar productos de ejemplo
            stmt.executeUpdate(
                    "INSERT INTO Producto (nombre, descripcion, precio, stock) VALUES " +
                            "('Mate Imperial', 'Calabaza forrada en cuero', 19999.90, 10), " +
                            "('Termo Acero 1L', 'Acero inoxidable', 34999.00, 25) " +
                            "ON DUPLICATE KEY UPDATE nombre=nombre"
            );

            System.out.println(" Base de datos y tablas listas.");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Paso 2: Crear hilos concurrentes para simular acceso simultáneo
        Thread t1 = new Thread(() -> updateTwoProducts(1, 2));
        Thread t2 = new Thread(() -> updateTwoProducts(2, 1));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(" Simulación terminada.");
    }

    private static void updateTwoProducts(int firstId, int secondId) {
        final int MAX_RETRIES = 5;
        int retries = 0;

        while (retries < MAX_RETRIES) {
            try (Connection conn = coneccion.getConnection()) {
                conn.setAutoCommit(false); // Inicio de transacción
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

                try {
                    // Actualizar primer producto
                    try (PreparedStatement ps1 = conn.prepareStatement(
                            "UPDATE Producto SET stock = stock - 1 WHERE id_producto = ?")) {
                        ps1.setInt(1, firstId);
                        ps1.executeUpdate();
                    }

                    // Pequeña pausa para aumentar probabilidad de deadlock
                    Thread.sleep(500);

                    // Actualizar segundo producto
                    try (PreparedStatement ps2 = conn.prepareStatement(
                            "UPDATE Producto SET stock = stock - 1 WHERE id_producto = ?")) {
                        ps2.setInt(1, secondId);
                        ps2.executeUpdate();
                    }

                    conn.commit(); // Confirmar cambios
                    System.out.printf("Thread %s committed successfully.%n", Thread.currentThread().getName());
                    break; // Si todo bien, salir del loop

                } catch (SQLException inner) {
                    conn.rollback(); // 🔹 Revertir cambios si hay error
                    throw inner; // Lanzar de nuevo para detectar deadlock
                }

            } catch (SQLException e) {
                if (isDeadlock(e)) {
                    retries++;
                    System.err.printf("️ Deadlock detectado en %s (intento %d). Reintentando...%n",
                            Thread.currentThread().getName(), retries);
                    try {
                        Thread.sleep(200 * retries); // Espera incremental antes de reintentar
                    } catch (InterruptedException ignored) {}
                } else {
                    System.err.printf("Error en %s: %s%n", Thread.currentThread().getName(), e.getMessage());
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (retries == MAX_RETRIES) {
            System.err.printf("%s falló después de %d intentos.%n", Thread.currentThread().getName(), retries);
        }
    }

    private static boolean isDeadlock(SQLException e) {
        return "40001".equals(e.getSQLState()) || e.getErrorCode() == 1213;
    }
}
