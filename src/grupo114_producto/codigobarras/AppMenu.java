/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    private void crearProducto() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Precio: ");
        double precio = Double.parseDouble(scanner.nextLine());

        // Ahora Producto se encarga de crear su propio Código de Barras a través del servicio
        Producto p = productoService.crear(nombre, descripcion, stock, precio);

        System.out.println("✅ Producto creado: " + p);
    }

    private void listarProductos() {
        System.out.println("\nListado de productos:");
        productoService.getAll().forEach(System.out::println);
    }

    private void actualizarProducto() {
        System.out.print("Ingrese ID del producto: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Nuevo precio: ");
        double precio = Double.parseDouble(scanner.nextLine());

        boolean ok = productoService.actualizar(id, nombre, precio);
        System.out.println(ok ? "✅ Actualizado correctamente." : "❌ Producto no encontrado.");
    }

    private void eliminarProducto() {
        System.out.print("Ingrese ID del producto: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean ok = productoService.eliminar(id);
        System.out.println(ok ? "🗑️ Eliminado lógicamente." : "❌ Producto no encontrado.");
    }

    private void buscarProductoPorId() {
        System.out.print("Ingrese ID del producto: ");
        int id = Integer.parseInt(scanner.nextLine());

        productoService.getById(id).ifPresentOrElse(
                p -> System.out.println("🔍 Encontrado: " + p),
                () -> System.out.println("❌ No existe un producto con ese ID.")
        );
    }
}


