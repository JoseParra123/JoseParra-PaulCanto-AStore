package astore.compras;

import astore.productos.Producto;

/** Una línea del carrito: un producto y cuántas piezas se llevan. */
public class ItemCarrito {
    private Producto producto;
    private int cantidad;

    public ItemCarrito(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El item necesita un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) {
        if (cantidad > 0) {
            this.cantidad = cantidad;
        } else {
            System.out.println("Error: La cantidad debe ser mayor a cero.");
        }
    }

    /** Suma más piezas del mismo producto. */
    public void aumentar(int piezas) {
        if (piezas > 0) {
            cantidad += piezas;
        }
    }

    /** Precio del producto por la cantidad. */
    public double getSubtotal() {
        return producto.getPrecioLista() * cantidad;
    }
}
