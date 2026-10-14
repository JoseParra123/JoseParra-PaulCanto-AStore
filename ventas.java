import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ventas implements Guardable {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String folioVenta;
    private Personal vendedor;
    private clientes cliente;
    private ArrayList<productos> listaProductos; 
    private double totalCobrado;
    private LocalDateTime fecha; 

    public ventas(String folioVenta, Personal vendedor, clientes cliente) {
        this.folioVenta = folioVenta;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.listaProductos = new ArrayList<>();
        this.totalCobrado = 0.0;
        this.fecha = LocalDateTime.now();
        guardar(); 
    }

    public void agregarproducto(productos producto) { 
        if (producto != null) {
            listaProductos.add(producto);
            totalCobrado = sumarProductos(); 
            System.out.println("Producto '" + producto.getNombreArticulo() + "' agregado a la venta.");
            GestorUsuario.registrar("VENTA " + folioVenta + ": producto " + producto.getCodigoSKU() + " agregado");
            guardar();
        } else {
            GestorUsuario.error("No se puede agregar un producto nulo a la venta " + folioVenta + ".");
        }
    }

    public double calcularTotal() {
        totalCobrado = sumarProductos();
        GestorUsuario.registrar("VENTA " + folioVenta + ": total calculado $" + totalCobrado);
        guardar();
        return totalCobrado;
    }

    
    private double sumarProductos() {
        double total = 0.0;
        for (productos p : listaProductos) {
            total += p.getPrecioLista();
        }
        return total;
    }

    public String getFolioVenta() { return folioVenta; }
    public Personal getVendedor() { return vendedor; }
    public clientes getCliente() { return cliente; }
    
    public List<productos> getListaProductos() { return Collections.unmodifiableList(listaProductos); }
    public double getTotalCobrado() { return totalCobrado; }
    public LocalDateTime getFecha() { return fecha; }

    
    @Override
    public String getNombreArchivo() { return "ventas"; }

    @Override
    public String[] getEncabezados() {
        return new String[] { "folioVenta", "idVendedor", "idCliente", "productosSKU", "totalCobrado", "fecha" };
    }

    @Override
    public String[] getValores() {
        StringBuilder skus = new StringBuilder();
        for (productos p : listaProductos) {
            if (skus.length() > 0) skus.append('|');
            skus.append(p.getCodigoSKU());
        }
        return new String[] {
            folioVenta,
            vendedor != null ? vendedor.getId() : "",
            cliente != null ? cliente.getId() : "",
            skus.toString(),
            String.valueOf(totalCobrado),
            fecha.format(FORMATO_FECHA)
        };
    }
}