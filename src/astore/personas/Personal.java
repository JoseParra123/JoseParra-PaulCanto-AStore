package astore.personas;

/** Empleado de la tienda (cajero, vendedor, etc.). */
public class Personal extends Persona {
    private String puestoRol;
    private String horarioLaboral;

    public Personal(String id, String nombreCompleto, String telefono, String puestoRol, String horarioLaboral) {
        super(id, nombreCompleto, telefono);
        this.puestoRol = puestoRol;
        this.horarioLaboral = horarioLaboral;
    }

    public String getPuestoRol() { return puestoRol; }
    public void setPuestoRol(String puestoRol) {
        this.puestoRol = puestoRol;
        guardar();
    }

    public String getHorarioLaboral() { return horarioLaboral; }
    public void setHorarioLaboral(String horarioLaboral) {
        this.horarioLaboral = horarioLaboral;
        guardar();
    }

    @Override
    public String getTipo() { return "Empleado"; }

    @Override
    public String getNombreArchivo() { return "personal"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"id", "nombre", "telefono", "puesto", "horario"};
    }

    @Override
    public String[] getValores() {
        return new String[]{getId(), getNombreCompleto(), getTelefono(), puestoRol, horarioLaboral};
    }
}
