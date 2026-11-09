package astore.compras;

import astore.personas.Personal;
import astore.util.Guardable;

/** Corte de caja de un turno: suma lo cobrado por cada método de pago y compara contra el efectivo contado. */
public class CierreCaja implements Guardable {
    private String folioCorte;
    private Personal responsable;
    private Terminal terminal;
    private double dineroInicial;
    private double totalEfectivo;
    private double totalTarjeta;
    private double totalCredencial;
    private double diferencia; // negativo = faltante, positivo = sobrante
    private boolean cerrado;

    public CierreCaja(String folioCorte, Personal responsable, Terminal terminal, double dineroInicial) {
        if (responsable == null || terminal == null) {
            throw new IllegalArgumentException("El corte necesita un responsable y una terminal.");
        }
        if (dineroInicial < 0) {
            throw new IllegalArgumentException("El dinero inicial no puede ser negativo.");
        }
        this.folioCorte = folioCorte;
        this.responsable = responsable;
        this.terminal = terminal;
        this.dineroInicial = dineroInicial;
    }

    /** Suma una venta cobrada al total del método de pago que se usó. */
    public void registrarVenta(Venta venta) {
        if (cerrado) {
            throw new IllegalStateException("El turno ya está cerrado.");
        }
        if (!venta.isPagada()) {
            throw new IllegalStateException("Solo se pueden registrar ventas cobradas.");
        }
        String metodo = venta.getMetodoPago().getNombre();
        if (metodo.equals("Efectivo")) {
            totalEfectivo += venta.getTotalCobrado();
        } else if (metodo.equals("Tarjeta")) {
            totalTarjeta += venta.getTotalCobrado();
        } else {
            totalCredencial += venta.getTotalCobrado();
        }
    }

    /** Efectivo que debería haber en la caja: dinero inicial más lo cobrado en efectivo. */
    public double calcularEfectivoEsperado() {
        return Math.round((dineroInicial + totalEfectivo) * 100) / 100.0;
    }

    /** Cierra el turno comparando el efectivo contado contra el esperado. Devuelve la diferencia. */
    public double cerrarTurno(double efectivoContado) {
        diferencia = Math.round((efectivoContado - calcularEfectivoEsperado()) * 100) / 100.0;
        cerrado = true;
        guardar();
        return diferencia;
    }

    /** Texto con el resumen del corte. */
    public String generarReporte() {
        String resultado = "Cuadra perfecto";
        if (diferencia < 0) resultado = "Faltante de $" + String.format("%.2f", -diferencia);
        if (diferencia > 0) resultado = "Sobrante de $" + String.format("%.2f", diferencia);
        return "CORTE " + folioCorte + " - " + terminal.getNombreTerminal()
                + "\nResponsable: " + responsable.getNombreCompleto()
                + "\nEfectivo: $" + String.format("%.2f", totalEfectivo)
                + " | Tarjeta: $" + String.format("%.2f", totalTarjeta)
                + " | Credencial: $" + String.format("%.2f", totalCredencial)
                + "\nResultado: " + resultado;
    }

    public String getFolioCorte() { return folioCorte; }
    public Personal getResponsable() { return responsable; }
    public Terminal getTerminal() { return terminal; }
    public double getDineroInicial() { return dineroInicial; }
    public double getTotalEfectivo() { return totalEfectivo; }
    public double getTotalTarjeta() { return totalTarjeta; }
    public double getTotalCredencial() { return totalCredencial; }
    public double getDiferencia() { return diferencia; }
    public boolean isCerrado() { return cerrado; }

    @Override
    public String getNombreArchivo() { return "cierres_caja"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"folio", "responsable", "terminal", "dinero_inicial", "efectivo",
                "tarjeta", "credencial", "diferencia", "cerrado"};
    }

    @Override
    public String[] getValores() {
        return new String[]{folioCorte, responsable.getId(), terminal.getIdEquipo(),
                String.valueOf(dineroInicial), String.valueOf(totalEfectivo),
                String.valueOf(totalTarjeta), String.valueOf(totalCredencial),
                String.valueOf(diferencia), String.valueOf(cerrado)};
    }
}
