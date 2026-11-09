package astore.productos;

import astore.util.Guardable;
import astore.util.GestorUsuario;
import java.time.LocalDate;

/** Existencias de un producto que están en exhibición (en la tienda). */
public class Inventario implements Guardable {
    private Producto producto;
    private int cantidadEnExhibicion;
    private int stockMinimo;
    private String ubicacionExhibidor;
    private String ultimoResurtido;

    public Inventario(Producto producto, int cantidadEnExhibicion, int stockMinimo, String ubicacionExhibidor) {
        if (producto == null) {
            throw new IllegalArgumentException("El inventario necesita un producto.");
        }
        if (cantidadEnExhibicion < 0 || stockMinimo < 0) {
            throw new IllegalArgumentException("La cantidad y el stock mínimo no pueden ser negativos.");
        }
        this.producto = producto;
        this.cantidadEnExhibicion = cantidadEnExhibicion;
        this.stockMinimo = stockMinimo;
        this.ubicacionExhibidor = ubicacionExhibidor;
        this.ultimoResurtido = "Sin resurtido";
    }

    public Producto getProducto() { return producto; }
    public int getCantidadEnExhibicion() { return cantidadEnExhibicion; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo >= 0) {
            this.stockMinimo = stockMinimo;
        } else {
            System.out.println("Error: El stock mínimo no puede ser negativo.");
        }
    }
    public String getUbicacionExhibidor() { return ubicacionExhibidor; }
    public void setUbicacionExhibidor(String ubicacionExhibidor) { this.ubicacionExhibidor = ubicacionExhibidor; }
    public String getUltimoResurtido() { return ultimoResurtido; }
    public void setUltimoResurtido(String ultimoResurtido) { this.ultimoResurtido = ultimoResurtido; }

    /** ¿Hay suficientes piezas para esta cantidad? */
    public boolean hayStock(int cantidad) {
        return cantidad > 0 && cantidadEnExhibicion >= cantidad;
    }

    /** Resta piezas cuando se vende. Lanza una excepción si no alcanza. */
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (cantidad > cantidadEnExhibicion) {
            throw new IllegalStateException("Stock insuficiente de " + producto.getNombreArticulo());
        }
        cantidadEnExhibicion -= cantidad;
        guardar();
    }

    /** Suma piezas cuando llega un resurtido. */
    public void agregarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        cantidadEnExhibicion += cantidad;
        ultimoResurtido = LocalDate.now().toString();
        guardar();
    }

    /** Corrige la cantidad después de contar físicamente el exhibidor. */
    public void ajustarConteoFisico(int conteo) {
        if (conteo < 0) {
            throw new IllegalArgumentException("El conteo no puede ser negativo.");
        }
        cantidadEnExhibicion = conteo;
        guardar();
    }

    /** Resta piezas dañadas o perdidas (merma). */
    public void registrarMerma(int cantidad) {
        descontarStock(cantidad);
        GestorUsuario.registrar("MERMA " + producto.getNombreArticulo() + ": " + cantidad + " pza(s)");
    }

    /** ¿Ya se llegó al stock mínimo? */
    public boolean alertarFaltante() {
        return cantidadEnExhibicion <= stockMinimo;
    }

    /** Texto para avisar que hay que pedir más piezas (o null si todavía hay suficientes). */
    public String solicitarResurtido() {
        if (!alertarFaltante()) {
            return null;
        }
        return "Resurtir " + producto.getNombreArticulo() + ": quedan " + cantidadEnExhibicion
                + " (mínimo " + stockMinimo + ").";
    }

    @Override
    public String getNombreArchivo() { return "inventario"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"sku", "cantidad", "stock_minimo", "ubicacion", "ultimo_resurtido"};
    }

    @Override
    public String[] getValores() {
        return new String[]{producto.getCodigoSKU(), String.valueOf(cantidadEnExhibicion),
                String.valueOf(stockMinimo), ubicacionExhibidor, ultimoResurtido};
    }
}
