public class productos {
    private String codigoSKU;
    private String nombreArticulo;
    private double precioLista;

    public productos(String codigoSKU, String nombreArticulo, double precioLista) {
        this.codigoSKU = codigoSKU;
        this.nombreArticulo = nombreArticulo;
        setPrecioLista(precioLista);
    }

    public String getCodigoSKU() { return codigoSKU; }
    public void setCodigoSKU(String codigoSKU) { this.codigoSKU = codigoSKU; }

    public String getNombreArticulo() { return nombreArticulo; }
    public void setNombreArticulo(String nombreArticulo) { this.nombreArticulo = nombreArticulo; }

    public double getPrecioLista() { return precioLista; }
    public void setPrecioLista(double precioLista) {
        if (precioLista >= 0) {
            this.precioLista = precioLista;
        } else {
            System.out.println("Error: El precio no puede ser negativo.");
        }
    }
}