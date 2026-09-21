public class inventario { 
    private String Producto;
    private int Cantidad_En_Exhibicion;
    private int Stock_Minimo;
    private String Ubicacion_Exhibidor;
    private String Ultimo_Resurtido;

    public String getProducto() { return Producto; }
    public void setProducto(String Producto) { this.Producto = Producto; }
    public int getCantidad_En_Exhibicion() { return Cantidad_En_Exhibicion; }
    public void setCantidad_En_Exhibicion(int Cantidad_En_Exhibicion) { this.Cantidad_En_Exhibicion = Cantidad_En_Exhibicion; }
    public int getStock_Minimo() { return Stock_Minimo; }
    public void setStock_Minimo(int Stock_Minimo) { this.Stock_Minimo = Stock_Minimo; }
    public String getUbicacion_Exhibidor() { return Ubicacion_Exhibidor; }
    public void setUbicacion_Exhibidor(String Ubicacion_Exhibidor) { this.Ubicacion_Exhibidor = Ubicacion_Exhibidor; }
    public String getUltimo_Resurtido() { return Ultimo_Resurtido; }
    public void setUltimo_Resurtido(String Ultimo_Resurtido) { this.Ultimo_Resurtido = Ultimo_Resurtido; }

    public void vincularProducto() {}
    public void descontarStock() {}
    public void alertarFaltante() {}
    public void solicitarResurtido() {}
    public void ajustarConteoFisico() {}
    public void registrarMerma() {}
    public void reservarParaLinea() {}
    public void auditarEstante() {}
}