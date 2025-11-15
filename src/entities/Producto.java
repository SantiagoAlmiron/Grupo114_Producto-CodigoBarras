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

    public Producto(int id, String nombre, String descripcion, int stock, double precio) {
        this.idProducto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.borrado = false;
    }
    
    public Producto(String nombre, String descripcion, int stock, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.borrado = false;
    }

    private String generarCodigoDeBarrasUnico() {
        return "PROD-" + idProducto + "-" + System.currentTimeMillis();
    }

    // --- Setters ---
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setBorrado(boolean borrado) { this.borrado = borrado; }
    public void setCodigoDeBarras(CodigoDeBarras codigo) {
    this.codigoDeBarras = codigo;
    
    if (codigo != null && codigo.getProducto() != this) {
        codigo.setProducto(this);
    }
}


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

