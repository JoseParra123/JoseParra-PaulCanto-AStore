public class Facturacion { 
    private String Folio_Fiscal_UUID;
    private String RFC_Cliente;
    private String Razon_Social;
    private String Direccion_Fiscal;
    private String Correo_Envio;
    private String Uso_CFDI;

    public String getFolio_Fiscal_UUID() { return Folio_Fiscal_UUID; }
    public void setFolio_Fiscal_UUID(String Folio_Fiscal_UUID) { this.Folio_Fiscal_UUID = Folio_Fiscal_UUID; }
    public String getRFC_Cliente() { return RFC_Cliente; }
    public void setRFC_Cliente(String RFC_Cliente) { this.RFC_Cliente = RFC_Cliente; }
    public String getRazon_Social() { return Razon_Social; }
    public void setRazon_Social(String Razon_Social) { this.Razon_Social = Razon_Social; }
    public String getDireccion_Fiscal() { return Direccion_Fiscal; }
    public void setDireccion_Fiscal(String Direccion_Fiscal) { this.Direccion_Fiscal = Direccion_Fiscal; }
    public String getCorreo_Envio() { return Correo_Envio; }
    public void setCorreo_Envio(String Correo_Envio) { this.Correo_Envio = Correo_Envio; }
    public String getUso_CFDI() { return Uso_CFDI; }
    public void setUso_CFDI(String Uso_CFDI) { this.Uso_CFDI = Uso_CFDI; }

    public void asignarDatosFiscales() {}
    public void validarRFC() {}
    public void generarFactura() {}
    public void timbrarSAT() {}
    public void enviarXML_PDF() {}
    public void cancelarFactura() {}
    public void consultarHistorial() {}
}