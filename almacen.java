public class almacen { 
    private String ID_Bodega;
    private String Producto;
    private int Cantidad_En_Bodega;
    private String Ubicacion_Pasillo;
    private String Lote_Ingreso;
    private String Fecha_Caducidad;

    public String getID_Bodega() { return ID_Bodega; }
    public void setID_Bodega(String ID_Bodega) { this.ID_Bodega = ID_Bodega; }
    public String getProducto() { return Producto; }
    public void setProducto(String Producto) { this.Producto = Producto; }
    public int getCantidad_En_Bodega() { return Cantidad_En_Bodega; }
    public void setCantidad_En_Bodega(int Cantidad_En_Bodega) { this.Cantidad_En_Bodega = Cantidad_En_Bodega; }
    public String getUbicacion_Pasillo() { return Ubicacion_Pasillo; }
    public void setUbicacion_Pasillo(String Ubicacion_Pasillo) { this.Ubicacion_Pasillo = Ubicacion_Pasillo; }
    public String getLote_Ingreso() { return Lote_Ingreso; }
    public void setLote_Ingreso(String Lote_Ingreso) { this.Lote_Ingreso = Lote_Ingreso; }
    public String getFecha_Caducidad() { return Fecha_Caducidad; }
    public void setFecha_Caducidad(String Fecha_Caducidad) { this.Fecha_Caducidad = Fecha_Caducidad; }

    public void asignarUbicacion() {}
    public void transferirATienda() {}
    public void recibirEmbarque() {}
    public void realizarAuditoria() {}
    public void devolverAProveedor() {}
    public void reorganizarEstantes() {}
    public void generarReporteFaltantes() {}
    public void controlarMermas() {}
}