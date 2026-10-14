public class proovedores implements Guardable { 
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

    public void registrarRazonSocial() { registrarAccion("registrarRazonSocial"); }
    public void asignarContacto() { registrarAccion("asignarContacto"); }
    public void crearOrdenCompra() { registrarAccion("crearOrdenCompra"); }
    public void registrarEntrega() { registrarAccion("registrarEntrega"); }
    public void evaluarServicio() { registrarAccion("evaluarServicio"); }
    public void gestionarPagos() { registrarAccion("gestionarPagos"); }
    public void solicitarCatalogo() { registrarAccion("solicitarCatalogo"); }
    public void actualizarCondiciones() { registrarAccion("actualizarCondiciones"); }

    // ---- Guardado en "usuario" ----
    // Esta clase no tiene constructor: llena el objeto con sus setters y llama a guardar() al terminar.
    @Override
    public String getNombreArchivo() { return "proveedores"; }

    @Override
    public String[] getEncabezados() {
        return new String[] { "ID_Proveedor", "Razon_Social", "Nombre_Contacto", "Telefono_Email", "Tipo_Suministro", "Dias_Credito" };
    }

    @Override
    public String[] getValores() {
        return new String[] { ID_Proveedor, Razon_Social, Nombre_Contacto, Telefono_Email, Tipo_Suministro, String.valueOf(Dias_Credito) };
    }
}