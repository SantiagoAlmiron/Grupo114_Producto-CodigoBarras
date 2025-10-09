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
    private Producto Producto;
    private boolean borrado;
    
    public CodigoDeBarras(int id, String codigo, Producto Producto) {
        idCodigoDeBarras = id;
        this.codigo = codigo;
        fechaCreacion = Tiempo.ahora();
        this.Producto = Producto;
        borrado = false;
    }

    @Override
    public String toString() {
        return "CodigoDeBarras{" + "idCodigoDeBarras=" + idCodigoDeBarras + ", codigo=" + codigo + ", fechaCreacion=" + fechaCreacion + ", Producto= " + Producto + ", borrado=" + borrado + '}';
    }
}
