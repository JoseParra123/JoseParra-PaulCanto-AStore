<<<<<<< Updated upstream
public class Ventas { 
=======
// Archivo: ventas.java
public class ventas { //[cite: 8, 10, 11]
>>>>>>> Stashed changes
    private String Folio_Venta;
    private String Fecha_y_Hora;
    private String Vendedor_Asignado;
    private double Subtotal;
    private double IVA;
    private double Descuento;
    private double Total_Cobrado;
    private String Metodo_Pago;

    public String getFolio_Venta() { return Folio_Venta; }
    public void setFolio_Venta(String Folio_Venta) { this.Folio_Venta = Folio_Venta; }
    public String getFecha_y_Hora() { return Fecha_y_Hora; }
    public void setFecha_y_Hora(String Fecha_y_Hora) { this.Fecha_y_Hora = Fecha_y_Hora; }
    public String getVendedor_Asignado() { return Vendedor_Asignado; }
    public void setVendedor_Asignado(String Vendedor_Asignado) { this.Vendedor_Asignado = Vendedor_Asignado; }
    public double getSubtotal() { return Subtotal; }
    public void setSubtotal(double Subtotal) { this.Subtotal = Subtotal; }
    public double getIVA() { return IVA; }
    public void setIVA(double IVA) { this.IVA = IVA; }
    public double getDescuento() { return Descuento; }
    public void setDescuento(double Descuento) { this.Descuento = Descuento; }
    public double getTotal_Cobrado() { return Total_Cobrado; }
    public void setTotal_Cobrado(double Total_Cobrado) { this.Total_Cobrado = Total_Cobrado; }
    public String getMetodo_Pago() { return Metodo_Pago; }
    public void setMetodo_Pago(String Metodo_Pago) { this.Metodo_Pago = Metodo_Pago; }

    public void generarFolioUnico() {}
    public void asociarVendedor() {}
    public void calcularTotal() {}
    public void generarTicket() {}
    public void enviarTicketCorreo() {}
    public void procesarPagoEfectivo() {}
    public void procesarPagoTarjeta() {}
    public void procesarPagoCredencial() {}
    public void aplicarPromocion() {}
    public void cancelarVenta() {}
    public void generarValeReembolso() {}
}