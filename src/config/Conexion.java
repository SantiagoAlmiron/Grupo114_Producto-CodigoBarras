package config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Connection connection = null;

    // ✅ Este método lo van a usar tus servicios (como ProductoService)
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Dotenv dotenv = Dotenv.load();
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String pass = dotenv.get("DB_PASS");

            connection = DriverManager.getConnection(url, user, pass);
        }
        return connection;
    }

    // ✅ Este main es opcional, solo para probar la conexión manualmente
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Conectado a MySQL correctamente!");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a MySQL:");
            e.printStackTrace();
        }
    }
}
