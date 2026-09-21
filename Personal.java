public class Personal extends Persona {
    private String puestoRol;
    private String horarioLaboral;

    public Personal(String id, String nombreCompleto, String telefono, String puestoRol, String horarioLaboral) {
        super(id, nombreCompleto, telefono);
        this.puestoRol = puestoRol;
        this.horarioLaboral = horarioLaboral;
    }

    public String getPuestoRol() { return puestoRol; }
    public void setPuestoRol(String puestoRol) { this.puestoRol = puestoRol; }
    public String getHorarioLaboral() { return horarioLaboral; }
    public void setHorarioLaboral(String horarioLaboral) { this.horarioLaboral = horarioLaboral; }
}