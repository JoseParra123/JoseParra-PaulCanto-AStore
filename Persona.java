public abstract class Persona implements Guardable {
    private String id;
    private String nombreCompleto;
    private String telefono;

}
    public Persona(String id, String nombreCompleto, String telefono) {
        this.id = id;
        this.telefono = telefono;
        
        if (esNombreValido(nombreCompleto)) {
            this.nombreCompleto = nombreCompleto;
        } else {
            GestorUsuario.error("El nombre no puede estar vacío.");
        }
    }

    public Persona() {}

    
    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
        guardar();
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) {
        if (esNombreValido(nombreCompleto)) {
            this.nombreCompleto = nombreCompleto;
            guardar();
        } else {
            GestorUsuario.error("El nombre no puede estar vacío.");
        }
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
        guardar();
    }

    private boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }
}