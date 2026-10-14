public interface Guardable {

    
    String getNombreArchivo();

    
    String[] getEncabezados();

    
    String[] getValores();

    
    default void guardar() {
        GestorUsuario.guardar(this);
    }

    
    default void registrarAccion(String metodo) {
        String[] valores = getValores();
        String clave = (valores != null && valores.length > 0 && valores[0] != null)
                ? valores[0] : "sin clave";
        GestorUsuario.registrar("ACCION " + getNombreArchivo() + " [" + clave + "] -> " + metodo + "()");
    }
}