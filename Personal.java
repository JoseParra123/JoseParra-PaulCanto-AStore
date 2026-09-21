public class Personal { 
    private String ID_Empleado;
    private String Nombre_Completo;
    private String CURP_RFC;
    private String Puesto_Rol;
    private String Horario_Laboral;
    private String Contacto_Emergencia;
    private String Estado_Activo;

    public String getID_Empleado() { return ID_Empleado; }
    public void setID_Empleado(String ID_Empleado) { this.ID_Empleado = ID_Empleado; }
    public String getNombre_Completo() { return Nombre_Completo; }
    public void setNombre_Completo(String Nombre_Completo) { this.Nombre_Completo = Nombre_Completo; }
    public String getCURP_RFC() { return CURP_RFC; }
    public void setCURP_RFC(String CURP_RFC) { this.CURP_RFC = CURP_RFC; }
    public String getPuesto_Rol() { return Puesto_Rol; }
    public void setPuesto_Rol(String Puesto_Rol) { this.Puesto_Rol = Puesto_Rol; }
    public String getHorario_Laboral() { return Horario_Laboral; }
    public void setHorario_Laboral(String Horario_Laboral) { this.Horario_Laboral = Horario_Laboral; }
    public String getContacto_Emergencia() { return Contacto_Emergencia; }
    public void setContacto_Emergencia(String Contacto_Emergencia) { this.Contacto_Emergencia = Contacto_Emergencia; }
    public String getEstado_Activo() { return Estado_Activo; }
    public void setEstado_Activo(String Estado_Activo) { this.Estado_Activo = Estado_Activo; }

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
}