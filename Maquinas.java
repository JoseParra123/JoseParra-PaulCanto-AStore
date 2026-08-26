public class Maquinas {

    // Atributos
    private String idEquipo;
    private String nombreTerminal;
    private String ipRed;
    private String tipoMaquinaria;
    private String estadoOperativo;
    private String ubicacion;

    // Constructor
    public Maquinas() {}

    // Métodos del UML
    public void asignarNombreEquipo() {}
    public void configurarIP() {}
    public void iniciarSesion() {}
    public void reportarFalla() {}
    public void imprimirTicket() {}
    public void calibrarLector() {}
    public void solicitarMantenimiento() {}
    public void reiniciarTerminal() {}
    public void actualizarSoftware() {}

    // Getters y Setters
    public String getIdEquipo() { return idEquipo; }
    public void setIdEquipo(String idEquipo) { this.idEquipo = idEquipo; }

    public String getNombreTerminal() { return nombreTerminal; }
    public void setNombreTerminal(String nombreTerminal) { this.nombreTerminal = nombreTerminal; }

    public String getIpRed() { return ipRed; }
    public void setIpRed(String ipRed) { this.ipRed = ipRed; }

    public String getTipoMaquinaria() { return tipoMaquinaria; }
    public void setTipoMaquinaria(String tipoMaquinaria) { this.tipoMaquinaria = tipoMaquinaria; }

    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}