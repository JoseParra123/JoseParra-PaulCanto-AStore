package astore.compras;

/**
 * Pago con tarjeta. Solo se guardan los últimos 4 dígitos.
 * No se conecta a ningún banco: solo revisa que los dígitos sean válidos.
 */
public class PagoTarjeta extends MetodoPago {
    private String ultimosDigitos;

    public PagoTarjeta(String ultimosDigitos) {
        super("Tarjeta");
        this.ultimosDigitos = ultimosDigitos;
    }

    @Override
    public boolean pagar(double monto) {
        return monto > 0 && ultimosDigitos != null && ultimosDigitos.matches("\\d{4}");
    }

    public String getUltimosDigitos() { return ultimosDigitos; }
}
