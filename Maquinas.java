public class Maquinas { //[cite: 5, 9]
    private String ID_Equipo;
    private String Nombre_Terminal;
    private String IP_Red;
    private String Tipo_Maquinaria;
    private String Estado_Operativo;
    private String Ubicacion;

    public String getID_Equipo() { return ID_Equipo; }
    public void setID_Equipo(String ID_Equipo) { this.ID_Equipo = ID_Equipo; }
    public String getNombre_Terminal() { return Nombre_Terminal; }
    public void setNombre_Terminal(String Nombre_Terminal) { this.Nombre_Terminal = Nombre_Terminal; }
    public String getIP_Red() { return IP_Red; }
    public void setIP_Red(String IP_Red) { this.IP_Red = IP_Red; }
    public String getTipo_Maquinaria() { return Tipo_Maquinaria; }
    public void setTipo_Maquinaria(String Tipo_Maquinaria) { this.Tipo_Maquinaria = Tipo_Maquinaria; }
    public String getEstado_Operativo() { return Estado_Operativo; }
    public void setEstado_Operativo(String Estado_Operativo) { this.Estado_Operativo = Estado_Operativo; }
    public String getUbicacion() { return Ubicacion; }
    public void setUbicacion(String Ubicacion) { this.Ubicacion = Ubicacion; }

    public void asignarNombreEquipo() {}
    public void configurarIP() {}
    public void iniciarSesion() {}
    public void reportarFalla() {}
    public void imprimirTicket() {}
    public void calibrarLector() {}
    public void solicitarMantenimiento() {}
    public void reiniciarTerminal() {}
    public void actualizarSoftware() {}
}