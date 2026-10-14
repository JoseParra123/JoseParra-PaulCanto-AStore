public class productos implements Guardable {
    private String codigoSKU;
    private String nombreArticulo;
    private double precioLista;

    public productos(String codigoSKU, String nombreArticulo, double precioLista) {
        this.codigoSKU = codigoSKU;
        this.nombreArticulo = nombreArticulo;
        asignarPrecio(precioLista);
        guardar(); 
    }

    public String getCodigoSKU() { return codigoSKU; }
    public void setCodigoSKU(String codigoSKU) {
        this.codigoSKU = codigoSKU;
        guardar();
    }

    public String getNombreArticulo() { return nombreArticulo; }
    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
        guardar();
    }

    public double getPrecioLista() { return precioLista; }
    public void setPrecioLista(double precioLista) {
        if (asignarPrecio(precioLista)) {
            guardar();
        }
    }

    
    private boolean asignarPrecio(double precioLista) {
        if (precioLista >= 0) {
            this.precioLista = precioLista;
            return true;
        }
        GestorUsuario.error("El precio no puede ser negativo.");
        return false;
    }

    
    @Override
    public String getNombreArchivo() { return "productos"; }

    @Override
    public String[] getEncabezados() {
        return new String[] { "codigoSKU", "nombreArticulo", "precioLista" };
    }

    @Override
    public String[] getValores() {
        return new String[] { codigoSKU, nombreArticulo, String.valueOf(precioLista) };
    }
}