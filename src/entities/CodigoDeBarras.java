/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;
import java.time.ZonedDateTime;
import services.Tiempo;

/**
 *
 * @author santiago
 */
public class CodigoDeBarras {
    private int idCodigoDeBarras;
    private String codigo;
    private ZonedDateTime fechaCreacion;
    private Producto producto;
    private boolean borrado;
    
    public CodigoDeBarras(int id, String codigo, Producto Producto) {
        idCodigoDeBarras = id;
        this.codigo = codigo;
        fechaCreacion = Tiempo.ahora();
        this.producto = Producto;
        borrado = false;
    }
    
    
    // -- Setters
    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }
    public void setProducto(Producto producto) {
    this.producto = producto;

    if (producto != null && producto.getCodigoDeBarras() != this) {
        producto.setCodigoDeBarras(this);
    }
}
    
    // -- Getters
    public int getCodigoDeBarrasId() { return idCodigoDeBarras; }
    public String getCodigo() { return codigo; }
    public boolean isBorrado() { return borrado; }
    public ZonedDateTime getFechaCreacion() { return fechaCreacion; }
    public Producto getProducto() { return producto; }

    @Override
    public String toString() {
        return "CodigoDeBarras{" + "idCodigoDeBarras=" + idCodigoDeBarras + ", codigo=" + codigo + ", fechaCreacion=" + fechaCreacion + ", Producto= " + producto + ", borrado=" + borrado + '}';
    }
}
