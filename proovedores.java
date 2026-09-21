public class proovedores { 
    private String ID_Proveedor;
    private String Razon_Social;
    private String Nombre_Contacto;
    private String Telefono_Email;
    private String Tipo_Suministro;
    private int Dias_Credito;

    public String getID_Proveedor() { return ID_Proveedor; }
    public void setID_Proveedor(String ID_Proveedor) { this.ID_Proveedor = ID_Proveedor; }
    public String getRazon_Social() { return Razon_Social; }
    public void setRazon_Social(String Razon_Social) { this.Razon_Social = Razon_Social; }
    public String getNombre_Contacto() { return Nombre_Contacto; }
    public void setNombre_Contacto(String Nombre_Contacto) { this.Nombre_Contacto = Nombre_Contacto; }
    public String getTelefono_Email() { return Telefono_Email; }
    public void setTelefono_Email(String Telefono_Email) { this.Telefono_Email = Telefono_Email; }
    public String getTipo_Suministro() { return Tipo_Suministro; }
    public void setTipo_Suministro(String Tipo_Suministro) { this.Tipo_Suministro = Tipo_Suministro; }
    public int getDias_Credito() { return Dias_Credito; }
    public void setDias_Credito(int Dias_Credito) { this.Dias_Credito = Dias_Credito; }

    public void registrarRazonSocial() {}
    public void asignarContacto() {}
    public void crearOrdenCompra() {}
    public void registrarEntrega() {}
    public void evaluarServicio() {}
    public void gestionarPagos() {}
    public void solicitarCatalogo() {}
    public void actualizarCondiciones() {}
}