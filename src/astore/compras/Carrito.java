package astore.compras;

import astore.productos.Producto;
import astore.productos.Promocion;
import java.util.ArrayList;

/** Carrito de compras: guarda una lista de ItemCarrito y calcula subtotal y total. */
public class Carrito {
    private ArrayList<ItemCarrito> items;

    public Carrito() {
        this.items = new ArrayList<>();
    }

    /** Agrega un producto. Si ya estaba en el carrito, solo suma la cantidad. */
    public void agregar(Producto producto, int cantidad) {
        ItemCarrito existente = buscar(producto.getCodigoSKU());
        if (existente != null) {
            existente.aumentar(cantidad);
        } else {
            items.add(new ItemCarrito(producto, cantidad));
        }
    }

    /** Quita un producto del carrito. Devuelve false si no estaba. */
    public boolean eliminar(String sku) {
        ItemCarrito item = buscar(sku);
        if (item == null) {
            return false;
        }
        items.remove(item);
        return true;
    }

    /** Cambia la cantidad de un producto. Con cantidad 0 o menos, lo quita. */
    public void cambiarCantidad(String sku, int cantidad) {
        if (cantidad <= 0) {
            eliminar(sku);
            return;
        }
        ItemCarrito item = buscar(sku);
        if (item != null) {
            item.setCantidad(cantidad);
        }
    }

    /** Suma de todos los subtotales (sin descuento). */
    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getSubtotal();
        }
        return redondear(subtotal);
    }

    /** Descuento que da una promoción (0 si no hay promoción). */
    public double calcularDescuento(Promocion promocion) {
        if (promocion == null) {
            return 0;
        }
        return promocion.calcularDescuento(calcularSubtotal());
    }

    /** Total a pagar: subtotal menos descuento. */
    public double calcularTotal(Promocion promocion) {
        return redondear(calcularSubtotal() - calcularDescuento(promocion));
    }

    public int contarArticulos() {
        int total = 0;
        for (ItemCarrito item : items) {
            total += item.getCantidad();
        }
        return total;
    }

    public void vaciar() { items.clear(); }
    public boolean estaVacio() { return items.isEmpty(); }
    public ArrayList<ItemCarrito> getItems() { return items; }

    private ItemCarrito buscar(String sku) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigoSKU().equals(sku)) {
                return item;
            }
        }
        return null;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}
