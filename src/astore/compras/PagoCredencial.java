package astore.compras;

import astore.personas.Cliente;

/** Pago con el crédito institucional de la credencial del cliente. */
public class PagoCredencial extends MetodoPago {
    private Cliente cliente;

    public PagoCredencial(Cliente cliente) {
        super("Credencial");
        this.cliente = cliente;
    }

    @Override
    public boolean pagar(double monto) {
        return cliente != null && cliente.usarCredito(monto);
    }

    public Cliente getCliente() { return cliente; }
}
