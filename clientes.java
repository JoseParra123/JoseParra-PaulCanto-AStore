public class clientes extends Persona {
    private String correoInstitucional;
    private int puntosLeones;
    private double creditoInstitucional;

    public clientes(String id, String nombreCompleto, String telefono, String correoInstitucional) {
        super(id, nombreCompleto, telefono); 
        this.correoInstitucional = correoInstitucional;
        this.puntosLeones = 0;
        this.creditoInstitucional = 0.0;
        guardar(); 
    }

    
    public void setPuntosLeones(int puntosLeones) {
        if (puntosLeones >= 0) {
            this.puntosLeones = puntosLeones;
            guardar();
        } else {
            GestorUsuario.error("Los puntos no pueden ser negativos.");
        }
    }

    public int getPuntosLeones() { return puntosLeones; }

    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
        guardar();
    }

    public double getCreditoInstitucional() { return creditoInstitucional; }
    public void setCreditoInstitucional(double creditoInstitucional) {
        
        if (creditoInstitucional >= 0) {
            this.creditoInstitucional = creditoInstitucional;
            guardar();
        } else {
            GestorUsuario.error("El crédito institucional no puede ser negativo.");
        }
    }

    
    @Override
    public String getNombreArchivo() { return "clientes"; }

    @Override
    public String[] getEncabezados() {
        return new String[] { "id", "nombreCompleto", "telefono", "correoInstitucional",
                              "puntosLeones", "creditoInstitucional" };
    }

    @Override
    public String[] getValores() {
        return new String[] { getId(), getNombreCompleto(), getTelefono(), correoInstitucional,
                              String.valueOf(puntosLeones), String.valueOf(creditoInstitucional) };
    }
}