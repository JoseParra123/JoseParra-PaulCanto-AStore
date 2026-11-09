package astore.compras;

import astore.util.Guardable;
import java.util.ArrayList;

/** Pedido hecho desde la página web. Se paga al recoger en la tienda. */
public class Pedido implements Guardable {
    public static final String PENDIENTE = "Pendiente";
    public static final String PAGADO = "Pagado";
    public static final String ENTREGADO = "Entregado";
    public static final String CANCELADO = "Cancelado";

    private String folio;
    private String fecha;
    private String nombreCliente;
    private String correo;
    private String metodoPago;
    private String estado;
    private String codigoPromocion;
    private double descuento;
    private double total;
    private ArrayList<DetallePedido> detalles; // composición: los detalles pertenecen al pedido

    public Pedido(String folio, String fecha, String nombreCliente, String correo, String metodoPago) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El pedido necesita el nombre del cliente.");
        }
        this.folio = folio;
        this.fecha = fecha;
        this.nombreCliente = nombreCliente.trim();
        this.correo = correo;
        this.metodoPago = metodoPago;
        this.estado = PENDIENTE;
        this.codigoPromocion = "";
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetallePedido d : detalles) {
            subtotal += d.getSubtotal();
        }
        return Math.round(subtotal * 100) / 100.0;
    }

    /** Guarda el descuento y calcula el total del pedido. */
    public void aplicarDescuento(String codigoPromocion, double descuento) {
        this.codigoPromocion = (codigoPromocion == null) ? "" : codigoPromocion;
        this.descuento = descuento;
        this.total = Math.round((calcularSubtotal() - descuento) * 100) / 100.0;
    }

    /** Cambia el estado del pedido (solo acepta los 4 estados válidos). */
    public void cambiarEstado(String nuevoEstado) {
        if (PENDIENTE.equals(nuevoEstado) || PAGADO.equals(nuevoEstado)
                || ENTREGADO.equals(nuevoEstado) || CANCELADO.equals(nuevoEstado)) {
            this.estado = nuevoEstado;
            guardar();
        } else {
            throw new IllegalArgumentException("Estado de pedido no válido: " + nuevoEstado);
        }
    }

    public String getFolio() { return folio; }
    public String getFecha() { return fecha; }
    public String getNombreCliente() { return nombreCliente; }
    public String getCorreo() { return correo; }
    public String getMetodoPago() { return metodoPago; }
    public String getEstado() { return estado; }
    public String getCodigoPromocion() { return codigoPromocion; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public ArrayList<DetallePedido> getDetalles() { return detalles; }

    /** Reconstruye un pedido a partir de una fila del archivo pedidos.csv. */
    public static Pedido desdeFila(String[] f) {
        Pedido pedido = new Pedido(f[0], f[1], f[2], f[3], f[4]);
        pedido.estado = f[5];
        pedido.codigoPromocion = f[6];
        pedido.descuento = Double.parseDouble(f[7]);
        pedido.total = Double.parseDouble(f[8]);
        if (!f[9].isEmpty()) {
            for (String texto : f[9].split("\\|")) {
                String[] partes = texto.split("~");
                pedido.detalles.add(new DetallePedido(partes[0], partes[3],
                        Double.parseDouble(partes[2]), Integer.parseInt(partes[1])));
            }
        }
        return pedido;
    }

    @Override
    public String getNombreArchivo() { return "pedidos"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"folio", "fecha", "cliente", "correo", "metodo_pago", "estado",
                "promocion", "descuento", "total", "detalle"};
    }

    @Override
    public String[] getValores() {
        // Cada detalle se guarda como sku~cantidad~precio~nombre y se separan con |
        StringBuilder texto = new StringBuilder();
        for (DetallePedido d : detalles) {
            if (texto.length() > 0) texto.append('|');
            texto.append(d.getSku()).append('~').append(d.getCantidad()).append('~')
                    .append(d.getPrecioUnitario()).append('~').append(d.getNombreProducto());
        }
        return new String[]{folio, fecha, nombreCliente, correo, metodoPago, estado,
                codigoPromocion, String.valueOf(descuento), String.valueOf(total), texto.toString()};
    }
}
