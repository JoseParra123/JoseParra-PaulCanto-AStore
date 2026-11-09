package astore.util;

/**
 * Interfaz para las clases que se guardan en un archivo CSV dentro de la carpeta "datos".
 * Cada clase dice en qué archivo se guarda, cómo se llaman sus columnas y qué valores tiene.
 * El primer valor siempre es la clave del registro (por ejemplo el SKU o el id).
 */
public interface Guardable {

    /** Nombre del archivo, sin extensión (ejemplo: "productos"). */
    String getNombreArchivo();

    /** Nombres de las columnas del archivo. */
    String[] getEncabezados();

    /** Valores del objeto, en el mismo orden que los encabezados. */
    String[] getValores();

    /** Guarda (o actualiza) este objeto en su archivo CSV. */
    default boolean guardar() {
        return GestorUsuario.guardar(this);
    }
}
