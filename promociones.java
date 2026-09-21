public class promociones { 
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

    public void definirNombreCampana() {}
    public void configurarReglas() {}
    public void validarVigencia() {}
    public void calcularDescuento() {}
    public void desactivarPromocion() {}
    public void evaluarImpactoVentas() {}
}