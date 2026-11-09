package astore.compras;

import astore.util.Guardable;

/**
 * Datos fiscales de una venta (RFC, razón social, uso de CFDI).
 * IMPORTANTE: es un comprobante interno de práctica. No se conecta al SAT ni timbra facturas reales.
 */
public class Factura implements Guardable {
    private String folioFactura;
    private Venta venta;
    private String rfcCliente;
    private String razonSocial;
    private String direccionFiscal;
    private String correoEnvio;
    private String usoCFDI;

    public Factura(Venta venta, String rfcCliente, String razonSocial,
                   String direccionFiscal, String correoEnvio, String usoCFDI) {
        if (venta == null || !venta.isPagada()) {
            throw new IllegalArgumentException("Solo se puede facturar una venta cobrada.");
        }
        if (!validarRFC(rfcCliente)) {
            throw new IllegalArgumentException("El RFC no tiene un formato válido.");
        }
        this.folioFactura = "FAC-" + venta.getFolioVenta();
        this.venta = venta;
        this.rfcCliente = rfcCliente.toUpperCase();
        this.razonSocial = razonSocial;
        this.direccionFiscal = direccionFiscal;
        this.correoEnvio = correoEnvio;
        this.usoCFDI = usoCFDI;
    }

    /** Revisa solo el formato del RFC: 3 o 4 letras, 6 dígitos (fecha) y 3 caracteres de homoclave. */
    public static boolean validarRFC(String rfc) {
        return rfc != null && rfc.toUpperCase().matches("[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}");
    }

    /** Resumen de la factura para mostrar en pantalla. */
    public String generarResumen() {
        return "Factura " + folioFactura + " | RFC: " + rfcCliente + " | " + razonSocial
                + " | Total: $" + String.format("%.2f", venta.getTotalCobrado())
                + " | Enviar a: " + correoEnvio + " (comprobante interno de práctica)";
    }

    public String getFolioFactura() { return folioFactura; }
    public Venta getVenta() { return venta; }
    public String getRfcCliente() { return rfcCliente; }
    public String getRazonSocial() { return razonSocial; }
    public String getDireccionFiscal() { return direccionFiscal; }
    public String getCorreoEnvio() { return correoEnvio; }
    public String getUsoCFDI() { return usoCFDI; }

    @Override
    public String getNombreArchivo() { return "facturas"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"folio", "venta", "rfc", "razon_social", "direccion", "correo", "uso_cfdi", "total"};
    }

    @Override
    public String[] getValores() {
        return new String[]{folioFactura, venta.getFolioVenta(), rfcCliente, razonSocial,
                direccionFiscal, correoEnvio, usoCFDI, String.valueOf(venta.getTotalCobrado())};
    }
}
