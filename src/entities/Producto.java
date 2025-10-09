/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import services.CodigoDeBarrasService;

/**
 * Representa un producto que crea su código de barras
 * a través del servicio CodigoDeBarrasService.
 * 
 * @author Santiago
 */
public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private int stock;
    private double precio;
    private CodigoDeBarras codigoDeBarras;
    private boolean borrado;

    // Servicio inyectado (puede venir de fuera o ser estático compartido)
    private static CodigoDeBarrasService codigoDeBarrasService = new CodigoDeBarrasService();

    public Producto(int id, String nombre, String descripcion, int stock, double precio) {
        this.idProducto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.borrado = false;

        // Usa el servicio para crear el código de barras
        String valorCodigo = generarCodigoDeBarrasUnico();
        this.codigoDeBarras = codigoDeBarrasService.crear(valorCodigo, this);
    }

    /** Permite inyectar un servicio diferente si se necesita (por ejemplo en tests) */
    public static void setCodigoDeBarrasService(CodigoDeBarrasService service) {
        codigoDeBarrasService = service;
    }

    /** Genera un valor de código de barras único (puedes ajustar la lógica según tus reglas) */
    private String generarCodigoDeBarrasUnico() {
        return "PROD-" + idProducto + "-" + System.currentTimeMillis();
    }

    // --- Setters ---
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setBorrado(boolean borrado) { this.borrado = borrado; }

    // --- Getters ---
    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getStock() { return stock; }
    public double getPrecio() { return precio; }
    public CodigoDeBarras getCodigoDeBarras() { return codigoDeBarras; }
    public boolean isBorrado() { return borrado; }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", stock=" + stock +
                ", precio=" + precio +
                ", codigoDeBarras=" + (codigoDeBarras != null ? codigoDeBarras.getCodigo() : "null") +
                ", borrado=" + borrado +
                '}';
    }
}

