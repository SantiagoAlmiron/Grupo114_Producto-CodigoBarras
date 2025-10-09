/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package grupo114_producto.codigobarras;
import entities.Producto;
import entities.CodigoDeBarras;

/**
 *
 * @author santiago
 */
public class Grupo114_ProductoCodigoBarras {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Producto p = new Producto(1, "Helado", "Es de vainilla", 12, 5.25);
        System.out.println(
            p.toString()
        );
        System.out.println("-----------------------------------------------");
        System.out.println("");
        
        CodigoDeBarras cdb = new CodigoDeBarras(1, "ABC123", p);
        System.out.println(
            cdb.toString()
        );
        System.out.println("-----------------------------------------------");
        System.out.println("");        
    }
    
}
