package astore.productos;

import astore.util.Guardable;

/** Un artículo que vende la tienda. Tiene una categoría (relación de asociación). */
public class Producto implements Guardable {
    private String codigoSKU;
    private String nombreArticulo;
    private double precioLista;
    private String descripcion;
    private Categoria categoria;
    private String imagen;

    /** Constructor corto (el que ya usaba el proyecto). */
    public Producto(String codigoSKU, String nombreArticulo, double precioLista) {
        this(codigoSKU, nombreArticulo, precioLista, "", null, "");
    }

    /** Constructor completo. */
    public Producto(String codigoSKU, String nombreArticulo, double precioLista,
                    String descripcion, Categoria categoria, String imagen) {
        if (codigoSKU == null || codigoSKU.trim().isEmpty()) {
            throw new IllegalArgumentException("El SKU no puede estar vacío.");
        }
        if (nombreArticulo == null || nombreArticulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del artículo no puede estar vacío.");
        }
        if (precioLista < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.codigoSKU = codigoSKU;
        this.nombreArticulo = nombreArticulo;
        this.precioLista = precioLista;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public String getCodigoSKU() { return codigoSKU; }
    public void setCodigoSKU(String codigoSKU) { this.codigoSKU = codigoSKU; }

    public String getNombreArticulo() { return nombreArticulo; }
    public void setNombreArticulo(String nombreArticulo) { this.nombreArticulo = nombreArticulo; }

    public double getPrecioLista() { return precioLista; }
    public void setPrecioLista(double precioLista) {
        if (precioLista >= 0) {
            this.precioLista = precioLista;
        } else {
            System.out.println("Error: El precio no puede ser negativo.");
        }
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    /** Precio después de aplicar un porcentaje de descuento (ejemplo: 10 = 10%). */
    public double calcularPrecioConDescuento(double porcentaje) {
        double precio = precioLista * (1 - porcentaje / 100.0);
        return Math.round(precio * 100) / 100.0;
    }

    @Override
    public String getNombreArchivo() { return "productos"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"sku", "nombre", "precio", "descripcion", "categoria", "imagen"};
    }

    @Override
    public String[] getValores() {
        String idCategoria = (categoria == null) ? "" : categoria.getId();
        return new String[]{codigoSKU, nombreArticulo, String.valueOf(precioLista),
                descripcion, idCategoria, imagen};
    }

    @Override
    public String toString() { return codigoSKU + " - " + nombreArticulo + " ($" + precioLista + ")"; }
}
