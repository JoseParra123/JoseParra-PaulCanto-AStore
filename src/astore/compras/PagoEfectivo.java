package astore.compras;

/** Pago en efectivo: se acepta si el dinero recibido alcanza, y se calcula el cambio. */
public class PagoEfectivo extends MetodoPago {
    private double efectivoRecibido;
    private double cambio;

    public PagoEfectivo(double efectivoRecibido) {
        super("Efectivo");
        this.efectivoRecibido = efectivoRecibido;
    }

    @Override
    public boolean pagar(double monto) {
        if (efectivoRecibido < monto) {
            return false;
        }
        cambio = Math.round((efectivoRecibido - monto) * 100) / 100.0;
        return true;
    }

    public double getEfectivoRecibido() { return efectivoRecibido; }
    public double getCambio() { return cambio; }
}
