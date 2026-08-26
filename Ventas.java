public class Ventas {

    // Atributos
    private String folioVenta;
    private String fechaYHora;
    private String vendedorAsignado;
    private double subtotal;
    private double iva;
    private double descuento;
    private double totalCobrado;
    private String metodoPago;

    // Constructor
    public Ventas() {}

    // Métodos del UML (firma base sin lógica)
    public void generarFolioUnico() {}
    public void asociarVendedor() {}
    public double calcularTotal() { return 0.0; }
    public void generarTicket() {}
    public void enviarTicketCorreo() {}
    public void procesarPagoEfectivo() {}
    public void procesarPagoTarjeta() {}
    public void procesarPagoCredencial() {}
    public void aplicarPromocion() {}
    public void cancelarVenta() {}
    public void generarValeReembolso() {}

    // Getters y Setters
    public String getFolioVenta() { return folioVenta; }
    public void setFolioVenta(String folioVenta) { this.folioVenta = folioVenta; }

    public String getFechaYHora() { return fechaYHora; }
    public void setFechaYHora(String fechaYHora) { this.fechaYHora = fechaYHora; }

    public String getVendedorAsignado() { return vendedorAsignado; }
    public void setVendedorAsignado(String vendedorAsignado) { this.vendedorAsignado = vendedorAsignado; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getTotalCobrado() { return totalCobrado; }
    public void setTotalCobrado(double totalCobrado) { this.totalCobrado = totalCobrado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}