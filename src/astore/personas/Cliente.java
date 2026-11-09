package astore.personas;

/** Estudiante o persona de la comunidad que compra en la tienda. Acumula "puntos Leones". */
public class Cliente extends Persona {
    private String correoInstitucional;
    private int puntosLeones;
    private double creditoInstitucional;

    public Cliente(String id, String nombreCompleto, String telefono, String correoInstitucional) {
        super(id, nombreCompleto, telefono); // Llama al constructor de la clase padre Persona
        if (correoInstitucional == null || !correoInstitucional.contains("@")) {
            throw new IllegalArgumentException("El correo institucional no es válido.");
        }
        this.correoInstitucional = correoInstitucional;
        this.puntosLeones = 0;
        this.creditoInstitucional = 0.0;
    }

    // Validaciones en Setters (Encapsulamiento)
    public void setPuntosLeones(int puntosLeones) {
        if (puntosLeones >= 0) {
            this.puntosLeones = puntosLeones;
            guardar();
        } else {
            System.out.println("Error: Los puntos no pueden ser negativos.");
        }
    }

    public int getPuntosLeones() { return puntosLeones; }
    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) {
        if (correoInstitucional != null && correoInstitucional.contains("@")) {
            this.correoInstitucional = correoInstitucional;
            guardar();
        } else {
            System.out.println("Error: El correo institucional no es válido.");
        }
    }

    public double getCreditoInstitucional() { return creditoInstitucional; }
    public void setCreditoInstitucional(double creditoInstitucional) {
        if (creditoInstitucional >= 0) {
            this.creditoInstitucional = creditoInstitucional;
            guardar();
        } else {
            System.out.println("Error: El crédito no puede ser negativo.");
        }
    }

    /** Suma puntos (por ejemplo, después de una compra). */
    public void agregarPuntos(int puntos) {
        if (puntos > 0) {
            puntosLeones += puntos;
            guardar();
        }
    }

    /** Cobra del crédito institucional. Devuelve false si no alcanza el saldo. */
    public boolean usarCredito(double monto) {
        if (monto <= 0 || monto > creditoInstitucional) {
            return false;
        }
        creditoInstitucional -= monto;
        guardar();
        return true;
    }

    @Override
    public String getTipo() { return "Cliente"; }

    @Override
    public String getNombreArchivo() { return "clientes"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"id", "nombre", "telefono", "correo", "puntos_leones", "credito"};
    }

    @Override
    public String[] getValores() {
        return new String[]{getId(), getNombreCompleto(), getTelefono(), correoInstitucional,
                String.valueOf(puntosLeones), String.valueOf(creditoInstitucional)};
    }
}
