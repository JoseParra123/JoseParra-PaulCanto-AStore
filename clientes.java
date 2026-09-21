public class clientes extends Persona {
    private String correoInstitucional;
    private int puntosLeones;
    private double creditoInstitucional;

    public clientes(String id, String nombreCompleto, String telefono, String correoInstitucional) {
        super(id, nombreCompleto, telefono); // Llama al constructor de la clase padre Persona
        this.correoInstitucional = correoInstitucional;
        this.puntosLeones = 0;
        this.creditoInstitucional = 0.0;
    }

    // Validaciones en Setters (Encapsulamiento)
    public void setPuntosLeones(int puntosLeones) {
        if (puntosLeones >= 0) {
            this.puntosLeones = puntosLeones;
        } else {
            System.out.println("Error: Los puntos no pueden ser negativos.");
        }
    }

    public int getPuntosLeones() { return puntosLeones; }
    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }
    public double getCreditoInstitucional() { return creditoInstitucional; }
    public void setCreditoInstitucional(double creditoInstitucional) { this.creditoInstitucional = creditoInstitucional; }
}