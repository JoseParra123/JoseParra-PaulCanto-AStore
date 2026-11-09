package astore.compras;

import astore.util.Guardable;

/** Caja registradora (terminal punto de venta) de la tienda. Antes se llamaba "Maquinas". */
public class Terminal implements Guardable {
    public static final String OPERATIVA = "Operativa";
    public static final String CON_FALLA = "Con falla";
    public static final String EN_MANTENIMIENTO = "En mantenimiento";

    private String idEquipo;
    private String nombreTerminal;
    private String ubicacion;
    private String estadoOperativo;

    public Terminal(String idEquipo, String nombreTerminal, String ubicacion) {
        if (idEquipo == null || idEquipo.trim().isEmpty()) {
            throw new IllegalArgumentException("La terminal necesita un id.");
        }
        this.idEquipo = idEquipo;
        this.nombreTerminal = nombreTerminal;
        this.ubicacion = ubicacion;
        this.estadoOperativo = OPERATIVA;
    }

    public String getIdEquipo() { return idEquipo; }
    public String getNombreTerminal() { return nombreTerminal; }
    public void setNombreTerminal(String nombreTerminal) { this.nombreTerminal = nombreTerminal; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstadoOperativo() { return estadoOperativo; }

    public boolean estaOperativa() {
        return OPERATIVA.equals(estadoOperativo);
    }

    public void reportarFalla(String motivo) {
        estadoOperativo = CON_FALLA;
        guardar();
        System.out.println("Falla reportada en " + nombreTerminal + ": " + motivo);
    }

    public String solicitarMantenimiento() {
        estadoOperativo = EN_MANTENIMIENTO;
        guardar();
        return "Mantenimiento solicitado para " + nombreTerminal;
    }

    public void marcarOperativa() {
        estadoOperativo = OPERATIVA;
        guardar();
    }

    /** Arma el texto del ticket de una venta ya cobrada. */
    public String imprimirTicket(Venta venta) {
        if (!estaOperativa()) {
            throw new IllegalStateException("La terminal " + nombreTerminal + " no está operativa.");
        }
        StringBuilder ticket = new StringBuilder();
        ticket.append("===== A-STORE ANÁHUAC CANCÚN =====\n");
        ticket.append("Folio: ").append(venta.getFolioVenta()).append("\n");
        for (ItemCarrito item : venta.getCarrito().getItems()) {
            ticket.append(item.getCantidad()).append(" x ")
                    .append(item.getProducto().getNombreArticulo())
                    .append("  $").append(String.format("%.2f", item.getSubtotal())).append("\n");
        }
        ticket.append("TOTAL: $").append(String.format("%.2f", venta.getTotalCobrado())).append("\n");
        if (venta.getMetodoPago() != null) {
            ticket.append("Pago: ").append(venta.getMetodoPago().getNombre()).append("\n");
        }
        return ticket.toString();
    }

    @Override
    public String getNombreArchivo() { return "terminales"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"id", "nombre", "ubicacion", "estado"};
    }

    @Override
    public String[] getValores() {
        return new String[]{idEquipo, nombreTerminal, ubicacion, estadoOperativo};
    }
}
