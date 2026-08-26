public class Personal {

    // Atributos
    private String idEmpleado;
    private String nombreCompleto;
    private String curpRfc;
    private String puestoRol;
    private String horarioLaboral;
    private String contactoEmergencia;
    private boolean estadoActivo;

    // Constructor
    public Personal() {}

    // Métodos del UML
    public void asignarNombre() {}
    public void establecerPuesto() {}
    public void asociarBiometrico() {}
    public void darDeAlta() {}
    public void modificarDatos() {}
    public void darDeBaja() {}
    public void registrarAsistencia() {}
    public void asignarCaja() {}
    public void solicitarPermiso() {}
    public void generarReporteHoras() {}
    public void evaluarDesempeno() {}
    public void cambiarContrasena() {}

    // Getters y Setters
    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCurpRfc() { return curpRfc; }
    public void setCurpRfc(String curpRfc) { this.curpRfc = curpRfc; }

    public String getPuestoRol() { return puestoRol; }
    public void setPuestoRol(String puestoRol) { this.puestoRol = puestoRol; }

    public String getHorarioLaboral() { return horarioLaboral; }
    public void setHorarioLaboral(String horarioLaboral) { this.horarioLaboral = horarioLaboral; }

    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }

    public boolean isEstadoActivo() { return estadoActivo; }
    public void setEstadoActivo(boolean estadoActivo) { this.estadoActivo = estadoActivo; }
}