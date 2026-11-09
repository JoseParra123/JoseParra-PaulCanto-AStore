package astore.productos;

import astore.util.Guardable;

/** Categoría de productos (Ropa, Accesorios, Papelería, etc.). */
public class Categoria implements Guardable {
    private String id;
    private String nombre;
    private String descripcion;

    public Categoria(String id, String nombre, String descripcion) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría necesita un id.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría necesita un nombre.");
        }
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        } else {
            System.out.println("Error: El nombre de la categoría no puede estar vacío.");
        }
    }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String getNombreArchivo() { return "categorias"; }

    @Override
    public String[] getEncabezados() { return new String[]{"id", "nombre", "descripcion"}; }

    @Override
    public String[] getValores() { return new String[]{id, nombre, descripcion}; }

    @Override
    public String toString() { return nombre; }
}
