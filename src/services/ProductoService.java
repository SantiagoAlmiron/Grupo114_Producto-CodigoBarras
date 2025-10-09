/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author santiago
 */

import entities.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoService {
    private List<Producto> productos = new ArrayList<>();
    private int nextId = 1;

    public Producto crear(String nombre, String descripcion, int stock, double precio) {
        Producto producto = new Producto(nextId++, nombre, descripcion, stock, precio);
        productos.add(producto);
        return producto;
    }

    public List<Producto> getAll() {
        return productos;
    }

    public Optional<Producto> getById(int id) {
        return productos.stream().filter(p -> p.getIdProducto() == id && !p.isBorrado()).findFirst();
    }

    public boolean actualizar(int id, String nuevoNombre, double nuevoPrecio) {
        Optional<Producto> opt = getById(id);
        if (opt.isPresent()) {
            Producto p = opt.get();
            p.setNombre(nuevoNombre);
            p.setPrecio(nuevoPrecio);
            return true;
        }
        return false;
    }

    public boolean eliminar(int id) {
        Optional<Producto> opt = getById(id);
        if (opt.isPresent()) {
            opt.get().setBorrado(true);
            return true;
        }
        return false;
    }
}
