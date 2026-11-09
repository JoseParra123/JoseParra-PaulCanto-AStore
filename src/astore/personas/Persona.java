package astore.personas;

import astore.util.Guardable;
import astore.util.GestorUsuario;

/**
 * Clase abstracta: es la base de Cliente, Personal y Administrador.
 * No se puede crear una "Persona" sola, solo sus hijas.
 */
public abstract class Persona implements Guardable {
    private String id;
    private String nombreCompleto;
    private String telefono;

    public Persona(String id, String nombreCompleto, String telefono) {
        if (!esNombreValido(nombreCompleto)) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
    }

    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
        guardar();
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) {
        if (esNombreValido(nombreCompleto)) {
            this.nombreCompleto = nombreCompleto;
            guardar();
        } else {
            GestorUsuario.error("El nombre no puede estar vacío.");
        }
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
        guardar();
    }

    /** Cada hija dice qué tipo de persona es (polimorfismo). */
    public abstract String getTipo();

    /** Texto corto para mostrar en pantalla. Usa getTipo() de la clase hija. */
    public String describir() {
        return getTipo() + ": " + nombreCompleto + " (" + id + ")";
    }

    private boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }
}
