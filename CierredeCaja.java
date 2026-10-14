public class CierredeCaja implements Guardable { 
    private String Folio_Corte;
    private String Responsable_Caja;
    private String Terminal_Asignada;
    private double Dinero_Inicial;
    private double Total_Efectivo;
    private double Total_Tarjeta;
    private double Total_Credencial;
    private double Diferencia_Faltante_Sobrante;

    public String getFolio_Corte() { return Folio_Corte; }
    public void setFolio_Corte(String Folio_Corte) { this.Folio_Corte = Folio_Corte; }
    public String getResponsable_Caja() { return Responsable_Caja; }
    public void setResponsable_Caja(String Responsable_Caja) { this.Responsable_Caja = Responsable_Caja; }
    public String getTerminal_Asignada() { return Terminal_Asignada; }
    public void setTerminal_Asignada(String Terminal_Asignada) { this.Terminal_Asignada = Terminal_Asignada; }
    public double getDinero_Inicial() { return Dinero_Inicial; }
    public void setDinero_Inicial(double Dinero_Inicial) { this.Dinero_Inicial = Dinero_Inicial; }
    public double getTotal_Efectivo() { return Total_Efectivo; }
    public void setTotal_Efectivo(double Total_Efectivo) { this.Total_Efectivo = Total_Efectivo; }
    public double getTotal_Tarjeta() { return Total_Tarjeta; }
    public void setTotal_Tarjeta(double Total_Tarjeta) { this.Total_Tarjeta = Total_Tarjeta; }
    public double getTotal_Credencial() { return Total_Credencial; }
    public void setTotal_Credencial(double Total_Credencial) { this.Total_Credencial = Total_Credencial; }
    public double getDiferencia_Faltante_Sobrante() { return Diferencia_Faltante_Sobrante; }
    public void setDiferencia_Faltante_Sobrante(double Diferencia_Faltante_Sobrante) { this.Diferencia_Faltante_Sobrante = Diferencia_Faltante_Sobrante; }

    public void asignarResponsable() { registrarAccion("asignarResponsable"); }
    public void abrirTurno() { registrarAccion("abrirTurno"); }
    public void realizarRetiroParcial() { registrarAccion("realizarRetiroParcial"); }
    public void realizarArqueoCiego() { registrarAccion("realizarArqueoCiego"); }
    public void compararIngresos() { registrarAccion("compararIngresos"); }
    public void cerrarTurno() { registrarAccion("cerrarTurno"); }
    public void generarReporteZ() { registrarAccion("generarReporteZ"); }
    public void notificarDiferencia() { registrarAccion("notificarDiferencia"); }

    // ---- Guardado en "usuario" ----
    // Esta clase no tiene constructor: llena el objeto con sus setters y llama a guardar() al terminar.
    @Override
    public String getNombreArchivo() { return "cierre_de_caja"; }

    @Override
    public String[] getEncabezados() {
        return new String[] { "Folio_Corte", "Responsable_Caja", "Terminal_Asignada", "Dinero_Inicial", "Total_Efectivo", "Total_Tarjeta", "Total_Credencial", "Diferencia_Faltante_Sobrante" };
    }

    @Override
    public String[] getValores() {
        return new String[] { Folio_Corte, Responsable_Caja, Terminal_Asignada, String.valueOf(Dinero_Inicial), String.valueOf(Total_Efectivo), String.valueOf(Total_Tarjeta), String.valueOf(Total_Credencial), String.valueOf(Diferencia_Faltante_Sobrante) };
    }
}