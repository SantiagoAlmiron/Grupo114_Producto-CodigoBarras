/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author santiago
 */
public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private int stock;
    private double precio;
    private CodigoDeBarras CodigoDeBarras;
    private boolean borrado;
    
    public Producto(int id, String nombre, String descripcion, int stock, double precio) {
        idProducto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.borrado = false;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCodigoDeBarras(CodigoDeBarras CodigoDeBarras) {
        this.CodigoDeBarras = CodigoDeBarras;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getStock() {
        return stock;
    }

    public double getPrecio() {
        return precio;
    }

    public CodigoDeBarras getCodigoDeBarras() {
        return this.CodigoDeBarras;
    }

    public boolean isBorrado() {
        return borrado;
    }

    @Override
    public String toString() {
        return "Producto{" + "id=" + idProducto + ", nombre=" + nombre + ", descripcion=" + descripcion + ", stock=" + stock + ", precio=" + precio + ", CodigoDeBarras=" + CodigoDeBarras + ", borrado=" + borrado + '}';
    }

    
}
