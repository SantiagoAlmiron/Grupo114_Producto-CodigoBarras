/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import entities.CodigoDeBarras;
import entities.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de manejar los códigos de barras de los productos.
 * Se encarga de crear, listar y eliminar (lógicamente) códigos de barras.
 * 
 * @author Santiago
 */
public class CodigoDeBarrasService {
    private final List<CodigoDeBarras> codigos = new ArrayList<>();
    private int nextId = 1;

    public CodigoDeBarras crear(String valor, Producto producto) {
        CodigoDeBarras c = new CodigoDeBarras(nextId++, valor, producto);
        codigos.add(c);
        return c;
    }

    public List<CodigoDeBarras> getAll() {
        return codigos;
    }

    public Optional<CodigoDeBarras> getById(int id) {
        return codigos.stream()
                .filter(c -> c.getCodigoDeBarrasId() == id && !c.isBorrado())
                .findFirst();
    }

    public boolean eliminar(int id) {
        Optional<CodigoDeBarras> opt = getById(id);
        if (opt.isPresent()) {
            opt.get().setBorrado(true);
            return true;
        }
        return false;
    }
}
