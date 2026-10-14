public class promociones implements Guardable { 
    private String Codigo_Promocion;
    private String Nombre_Campana;
    private String Descripcion_Regla;
    private double Porcentaje_Descuento;
    private String Vigencia;
    private String Perfil_Exclusivo;

    public String getCodigo_Promocion() { return Codigo_Promocion; }
    public void setCodigo_Promocion(String Codigo_Promocion) { this.Codigo_Promocion = Codigo_Promocion; }
    public String getNombre_Campana() { return Nombre_Campana; }
    public void setNombre_Campana(String Nombre_Campana) { this.Nombre_Campana = Nombre_Campana; }
    public String getDescripcion_Regla() { return Descripcion_Regla; }
    public void setDescripcion_Regla(String Descripcion_Regla) { this.Descripcion_Regla = Descripcion_Regla; }
    public double getPorcentaje_Descuento() { return Porcentaje_Descuento; }
    public void setPorcentaje_Descuento(double Porcentaje_Descuento) { this.Porcentaje_Descuento = Porcentaje_Descuento; }
    public String getVigencia() { return Vigencia; }
    public void setVigencia(String Vigencia) { this.Vigencia = Vigencia; }
    public String getPerfil_Exclusivo() { return Perfil_Exclusivo; }
    public void setPerfil_Exclusivo(String Perfil_Exclusivo) { this.Perfil_Exclusivo = Perfil_Exclusivo; }

    public void definirNombreCampana() { registrarAccion("definirNombreCampana"); }
    public void configurarReglas() { registrarAccion("configurarReglas"); }
    public void validarVigencia() { registrarAccion("validarVigencia"); }
    public void calcularDescuento() { registrarAccion("calcularDescuento"); }
    public void desactivarPromocion() { registrarAccion("desactivarPromocion"); }
    public void evaluarImpactoVentas() { registrarAccion("evaluarImpactoVentas"); }

    // ---- Guardado en "usuario" ----
    // Esta clase no tiene constructor: llena el objeto con sus setters y llama a guardar() al terminar.
    @Override
    public String getNombreArchivo() { return "promociones"; }

    @Override
    public String[] getEncabezados() {
        return new String[] { "Codigo_Promocion", "Nombre_Campana", "Descripcion_Regla", "Porcentaje_Descuento", "Vigencia", "Perfil_Exclusivo" };
    }

    @Override
    public String[] getValores() {
        return new String[] { Codigo_Promocion, Nombre_Campana, Descripcion_Regla, String.valueOf(Porcentaje_Descuento), Vigencia, Perfil_Exclusivo };
    }
}