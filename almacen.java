public class almacen implements Guardable { 
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

    public void asignarUbicacion() { registrarAccion("asignarUbicacion"); }
    public void transferirATienda() { registrarAccion("transferirATienda"); }
    public void recibirEmbarque() { registrarAccion("recibirEmbarque"); }
    public void realizarAuditoria() { registrarAccion("realizarAuditoria"); }
    public void devolverAProveedor() { registrarAccion("devolverAProveedor"); }
    public void reorganizarEstantes() { registrarAccion("reorganizarEstantes"); }
    public void generarReporteFaltantes() { registrarAccion("generarReporteFaltantes"); }
    public void controlarMermas() { registrarAccion("controlarMermas"); }

    // ---- Guardado en "usuario" ----
    // Esta clase no tiene constructor: llena el objeto con sus setters y llama a guardar() al terminar.
    @Override
    public String getNombreArchivo() { return "almacen"; }

    @Override
    public String[] getEncabezados() {
        return new String[] { "ID_Bodega", "Producto", "Cantidad_En_Bodega", "Ubicacion_Pasillo", "Lote_Ingreso", "Fecha_Caducidad" };
    }

    @Override
    public String[] getValores() {
        return new String[] { ID_Bodega, Producto, String.valueOf(Cantidad_En_Bodega), Ubicacion_Pasillo, Lote_Ingreso, Fecha_Caducidad };
    }
}