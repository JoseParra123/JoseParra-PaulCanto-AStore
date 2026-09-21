public class Productos { 
    private String Codigo_SKU;
    private String Nombre_Articulo;
    private String Descripcion;
    private String Categoria_Anahuac;
    private double Precio_Lista;
    private double Precio_Costo;
    private String Codigo_Barras;
    private String Imagen_Producto;

    public String getCodigo_SKU() { return Codigo_SKU; }
    public void setCodigo_SKU(String Codigo_SKU) { this.Codigo_SKU = Codigo_SKU; }
    public String getNombre_Articulo() { return Nombre_Articulo; }
    public void setNombre_Articulo(String Nombre_Articulo) { this.Nombre_Articulo = Nombre_Articulo; }
    public String getDescripcion() { return Descripcion; }
    public void setDescripcion(String Descripcion) { this.Descripcion = Descripcion; }
    public String getCategoria_Anahuac() { return Categoria_Anahuac; }
    public void setCategoria_Anahuac(String Categoria_Anahuac) { this.Categoria_Anahuac = Categoria_Anahuac; }
    public double getPrecio_Lista() { return Precio_Lista; }
    public void setPrecio_Lista(double Precio_Lista) { this.Precio_Lista = Precio_Lista; }
    public double getPrecio_Costo() { return Precio_Costo; }
    public void setPrecio_Costo(double Precio_Costo) { this.Precio_Costo = Precio_Costo; }
    public String getCodigo_Barras() { return Codigo_Barras; }
    public void setCodigo_Barras(String Codigo_Barras) { this.Codigo_Barras = Codigo_Barras; }
    public String getImagen_Producto() { return Imagen_Producto; }
    public void setImagen_Producto(String Imagen_Producto) { this.Imagen_Producto = Imagen_Producto; }

    public void asignarNombreArticulo() {}
    public void vincularCategoria() {}
    public void subirFotografia() {}
    public void generarSKU() {}
    public void actualizarPrecio() {}
    public void aplicarDescuento() {}
    public void verFichaTecnica() {}
    public void imprimirEtiqueta() {}
    public void clasificarPorTemporada() {}
    public void darDeBaja() {}
}