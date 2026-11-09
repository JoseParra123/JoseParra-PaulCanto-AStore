package astore.compras;

/** Una línea de un pedido. Guarda el precio del momento, aunque después cambie el precio del producto. */
public class DetallePedido {
    private String sku;
    private String nombreProducto;
    private double precioUnitario;
    private int cantidad;

    public DetallePedido(String sku, String nombreProducto, double precioUnitario, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.sku = sku;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    /** Crea el detalle a partir de una línea del carrito. */
    public DetallePedido(ItemCarrito item) {
        this(item.getProducto().getCodigoSKU(), item.getProducto().getNombreArticulo(),
                item.getProducto().getPrecioLista(), item.getCantidad());
    }

    public String getSku() { return sku; }
    public String getNombreProducto() { return nombreProducto; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }

    public double getSubtotal() {
        return Math.round(precioUnitario * cantidad * 100) / 100.0;
    }
}
