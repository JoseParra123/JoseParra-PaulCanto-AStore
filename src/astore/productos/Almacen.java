package astore.productos;

import astore.util.Guardable;
import java.time.LocalDate;

/** Existencias de un producto que están guardadas en la bodega (no en exhibición). */
public class Almacen implements Guardable {
    private String idBodega;
    private Producto producto;
    private int cantidadEnBodega;
    private String ubicacionPasillo;
    private String loteIngreso;
    private String fechaCaducidad; // "N/A" si el producto no caduca

    public Almacen(String idBodega, Producto producto, int cantidadEnBodega,
                   String ubicacionPasillo, String loteIngreso) {
        if (idBodega == null || idBodega.trim().isEmpty()) {
            throw new IllegalArgumentException("La bodega necesita un id.");
        }
        if (producto == null) {
            throw new IllegalArgumentException("La bodega necesita un producto.");
        }
        if (cantidadEnBodega < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.idBodega = idBodega;
        this.producto = producto;
        this.cantidadEnBodega = cantidadEnBodega;
        this.ubicacionPasillo = ubicacionPasillo;
        this.loteIngreso = loteIngreso;
        this.fechaCaducidad = "N/A";
    }

    public String getIdBodega() { return idBodega; }
    public Producto getProducto() { return producto; }
    public int getCantidadEnBodega() { return cantidadEnBodega; }
    public String getUbicacionPasillo() { return ubicacionPasillo; }
    public void setUbicacionPasillo(String ubicacionPasillo) { this.ubicacionPasillo = ubicacionPasillo; }
    public String getLoteIngreso() { return loteIngreso; }
    public String getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(String fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    /** Suma piezas que llegan de un proveedor. */
    public void recibirEmbarque(int cantidad, String lote) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del embarque debe ser mayor a cero.");
        }
        cantidadEnBodega += cantidad;
        loteIngreso = lote;
        guardar();
    }

    /** Pasa piezas de la bodega al exhibidor. Devuelve false si no alcanzan. */
    public boolean transferirATienda(Inventario inventario, int cantidad) {
        if (cantidad <= 0 || cantidad > cantidadEnBodega) {
            return false;
        }
        cantidadEnBodega -= cantidad;
        inventario.agregarStock(cantidad);
        guardar();
        return true;
    }

    /** Regresa piezas al proveedor (por ejemplo, si llegaron dañadas). */
    public boolean devolverAProveedor(int cantidad) {
        if (cantidad <= 0 || cantidad > cantidadEnBodega) {
            return false;
        }
        cantidadEnBodega -= cantidad;
        guardar();
        return true;
    }

    /** ¿Ya pasó la fecha de caducidad? Los productos sin caducidad nunca caducan. */
    public boolean estaCaducado(LocalDate hoy) {
        if (fechaCaducidad == null || fechaCaducidad.equals("N/A")) {
            return false;
        }
        return LocalDate.parse(fechaCaducidad).isBefore(hoy);
    }

    @Override
    public String getNombreArchivo() { return "almacen"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"id_bodega", "sku", "cantidad", "pasillo", "lote", "caducidad"};
    }

    @Override
    public String[] getValores() {
        return new String[]{idBodega, producto.getCodigoSKU(), String.valueOf(cantidadEnBodega),
                ubicacionPasillo, loteIngreso, fechaCaducidad};
    }
}
