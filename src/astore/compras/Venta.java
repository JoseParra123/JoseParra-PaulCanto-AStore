package astore.compras;

import astore.personas.Cliente;
import astore.personas.Personal;
import astore.productos.Producto;
import astore.util.Guardable;

/** Venta hecha en el mostrador: un empleado le vende a un cliente y se cobra con un MetodoPago. */
public class Venta implements Guardable {
    private String folioVenta;
    private Personal vendedor;
    private Cliente cliente;
    private Carrito carrito;       // composición: el carrito nace y muere con la venta
    private MetodoPago metodoPago; // se asigna al cobrar
    private double totalCobrado;
    private boolean pagada;

    public Venta(String folioVenta, Personal vendedor, Cliente cliente) {
        this.folioVenta = folioVenta;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.carrito = new Carrito();
        this.totalCobrado = 0.0;
        this.pagada = false;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (pagada) {
            throw new IllegalStateException("La venta ya fue cobrada.");
        }
        if (producto != null) {
            carrito.agregar(producto, cantidad);
            System.out.println("Producto '" + producto.getNombreArticulo() + "' agregado a la venta.");
        }
    }

    public double calcularTotal() {
        return carrito.calcularTotal(null);
    }

    /** Cobra la venta con el método de pago recibido. Devuelve true si el pago fue aceptado. */
    public boolean cobrar(MetodoPago metodo) {
        if (pagada) {
            throw new IllegalStateException("La venta ya fue cobrada.");
        }
        if (carrito.estaVacio()) {
            throw new IllegalStateException("La venta no tiene productos.");
        }
        double total = calcularTotal();
        if (!metodo.pagar(total)) {
            return false;
        }
        this.metodoPago = metodo;
        this.totalCobrado = total;
        this.pagada = true;
        if (cliente != null) {
            cliente.agregarPuntos((int) (total / 100)); // 1 punto por cada $100
        }
        guardar();
        return true;
    }

    public String getFolioVenta() { return folioVenta; }
    public Personal getVendedor() { return vendedor; }
    public Cliente getCliente() { return cliente; }
    public Carrito getCarrito() { return carrito; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public double getTotalCobrado() { return totalCobrado; }
    public boolean isPagada() { return pagada; }

    @Override
    public String getNombreArchivo() { return "ventas"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"folio", "vendedor", "cliente", "metodo_pago", "total", "pagada"};
    }

    @Override
    public String[] getValores() {
        return new String[]{folioVenta,
                vendedor == null ? "" : vendedor.getId(),
                cliente == null ? "" : cliente.getId(),
                metodoPago == null ? "" : metodoPago.getNombre(),
                String.valueOf(totalCobrado), String.valueOf(pagada)};
    }
}
