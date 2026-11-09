package astore.productos;

import astore.util.Guardable;
import java.time.LocalDate;

/** Empresa que le surte productos a la tienda. */
public class Proveedor implements Guardable {
    private String idProveedor;
    private String razonSocial;
    private String nombreContacto;
    private String telefono;
    private String correo;
    private String tipoSuministro;
    private int diasCredito;

    public Proveedor(String idProveedor, String razonSocial, String nombreContacto,
                     String telefono, String correo, String tipoSuministro, int diasCredito) {
        if (idProveedor == null || idProveedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El proveedor necesita un id.");
        }
        if (razonSocial == null || razonSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social no puede estar vacía.");
        }
        if (diasCredito < 0) {
            throw new IllegalArgumentException("Los días de crédito no pueden ser negativos.");
        }
        this.idProveedor = idProveedor;
        this.razonSocial = razonSocial;
        this.nombreContacto = nombreContacto;
        this.telefono = telefono;
        this.correo = correo;
        this.tipoSuministro = tipoSuministro;
        this.diasCredito = diasCredito;
    }

    public String getIdProveedor() { return idProveedor; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getNombreContacto() { return nombreContacto; }
    public void setNombreContacto(String nombreContacto) { this.nombreContacto = nombreContacto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTipoSuministro() { return tipoSuministro; }
    public void setTipoSuministro(String tipoSuministro) { this.tipoSuministro = tipoSuministro; }
    public int getDiasCredito() { return diasCredito; }

    public void setDiasCredito(int diasCredito) {
        if (diasCredito >= 0) {
            this.diasCredito = diasCredito;
        } else {
            System.out.println("Error: Los días de crédito no pueden ser negativos.");
        }
    }

    /** Fecha límite para pagarle al proveedor según sus días de crédito. */
    public LocalDate calcularFechaPago(LocalDate fechaCompra) {
        return fechaCompra.plusDays(diasCredito);
    }

    /** Devuelve el texto de una orden de compra para un producto. */
    public String crearOrdenCompra(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            throw new IllegalArgumentException("La orden necesita un producto y una cantidad mayor a cero.");
        }
        return "Orden de compra a " + razonSocial + ": " + cantidad + " pza(s) de "
                + producto.getNombreArticulo() + ". Pago a " + diasCredito + " días.";
    }

    @Override
    public String getNombreArchivo() { return "proveedores"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"id", "razon_social", "contacto", "telefono", "correo", "suministro", "dias_credito"};
    }

    @Override
    public String[] getValores() {
        return new String[]{idProveedor, razonSocial, nombreContacto, telefono, correo,
                tipoSuministro, String.valueOf(diasCredito)};
    }
}
