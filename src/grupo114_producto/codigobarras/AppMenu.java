package grupo114_producto.codigobarras;

import entities.Producto;
import services.CodigoDeBarrasService;
import services.ProductoService;

import java.util.Scanner;

/**
 * Menú de consola para gestionar productos y códigos de barras
 * @author Santiago
 */
public class AppMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final CodigoDeBarrasService codigoBarrasService = new CodigoDeBarrasService();
    private final ProductoService productoService = new ProductoService();

    public void iniciar() {
        String opcion;
        do {
            mostrarMenuPrincipal();
            opcion = scanner.nextLine().trim().toUpperCase();

            switch (opcion) {
                case "1" -> crearProducto();
                case "2" -> listarProductos();
                case "3" -> actualizarProducto();
                case "4" -> eliminarProducto();
                case "5" -> buscarProductoPorId();
                case "X" -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (!opcion.equals("X"));
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1) Crear producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Buscar producto por ID");
        System.out.println("X) Salir");
        System.out.print("Seleccione una opción: ");
    }

    // Métodos de entrada segura
    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un número entero válido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un número decimal válido.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("Error: el texto no puede estar vacío.");
        }
    }

    private void crearProducto() {
        String nombre = leerTexto("Nombre: ");
        String descripcion = leerTexto("Descripción: ");
        int stock = leerEntero("Stock: ");
        double precio = leerDouble("Precio: ");

        Producto p = productoService.crear(nombre, descripcion, stock, precio);

        if (p != null) {
            System.out.println("Producto creado: " + p);
        } else {
            System.out.println("No se pudo crear el producto.");
        }
    }

    private void listarProductos() {
        System.out.println("\nListado de productos:");
        productoService.getAll().forEach(System.out::println);
    }

    private void actualizarProducto() {
        int id = leerEntero("Ingrese ID del producto: ");
        String nombre = leerTexto("Nuevo nombre: ");
        double precio = leerDouble("Nuevo precio: ");

        boolean actualizado = productoService.actualizar(id, nombre, precio);
        System.out.println(actualizado ? "Producto actualizado correctamente." : "Producto no encontrado.");
    }

    private void eliminarProducto() {
        int id = leerEntero("Ingrese ID del producto: ");

        boolean eliminado = productoService.eliminar(id);
        System.out.println(eliminado ? "Producto eliminado lógicamente." : "Producto no encontrado.");
    }

    private void buscarProductoPorId() {
        int id = leerEntero("Ingrese ID del producto: ");

        productoService.getById(id).ifPresentOrElse(
                p -> System.out.println("Producto encontrado: " + p),
                () -> System.out.println("No existe un producto con ese ID.")
        );
    }
}