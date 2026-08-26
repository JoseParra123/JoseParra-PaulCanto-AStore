class CierreDeCaja {

    // Atributos
    private String folioCorte;
    private String responsableCaja;
    private String terminalAsignada;
    private double dineroInicial;
    private double totalEfectivo;
    private double totalTarjeta;
    private double totalCredencial;
    private double diferenciaFaltanteSobrante;

    // Constructor
    public CierreDeCaja() {}

    // Métodos del UML
    public void asignarResponsable() {}
    public void abrirTurno() {}
    public void realizarRetiroParcial() {}
    public void realizarArqueoCiego() {}
    public void compararIngresos() {}
    public void cerrarTurno() {}
    public void generarReporteZ() {}
    public void notificarDiferencia() {}

    // Getters y Setters
    public String getFolioCorte() { return folioCorte; }
    public void setFolioCorte(String folioCorte) { this.folioCorte = folioCorte; }

    public String getResponsableCaja() { return responsableCaja; }
    public void setResponsableCaja(String responsableCaja) { this.responsableCaja = responsableCaja; }

    public String getTerminalAsignada() { return terminalAsignada; }
    public void setTerminalAsignada(String terminalAsignada) { this.terminalAsignada = terminalAsignada; }

    public double getDineroInicial() { return dineroInicial; }
    public void setDineroInicial(double dineroInicial) { this.dineroInicial = dineroInicial; }

    public double getTotalEfectivo() { return totalEfectivo; }
    public void setTotalEfectivo(double totalEfectivo) { this.totalEfectivo = totalEfectivo; }

    public double getTotalTarjeta() { return totalTarjeta; }
    public void setTotalTarjeta(double totalTarjeta) { this.totalTarjeta = totalTarjeta; }

    public double getTotalCredencial() { return totalCredencial; }
    public void setTotalCredencial(double totalCredencial) { this.totalCredencial = totalCredencial; }

    public double getDiferenciaFaltanteSobrante() { return diferenciaFaltanteSobrante; }
    public void setDiferenciaFaltanteSobrante(double diferenciaFaltanteSobrante) { this.diferenciaFaltanteSobrante = diferenciaFaltanteSobrante; }
}