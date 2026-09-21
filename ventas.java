import java.util.ArrayList;

public class ventas {
    private String folioVenta;
    private Personal vendedor;
    private clientes cliente;
    private ArrayList<productos> listaProductos; // Usa la clase en minúscula
    private double totalCobrado;

    public ventas(String folioVenta, Personal vendedor, clientes cliente) {
        this.folioVenta = folioVenta;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.listaProductos = new ArrayList<>();
        this.totalCobrado = 0.0;
    }

    public void agregarproducto(productos producto) { // En minúsculas
        if (producto != null) {
            listaProductos.add(producto);
            System.out.println("Producto '" + producto.getNombreArticulo() + "' agregado a la venta.");
        }
    }

    public double calcularTotal() {
        totalCobrado = 0.0;
        for (productos p : listaProductos) {
            totalCobrado += p.getPrecioLista();
        }
        return totalCobrado;
    }

    public String getFolioVenta() { return folioVenta; }
    public Personal getVendedor() { return vendedor; }
    public clientes getCliente() { return cliente; }
    public ArrayList<productos> getListaProductos() { return listaProductos; }
    public double getTotalCobrado() { return totalCobrado; }
}