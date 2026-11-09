package astore.personas;

import astore.productos.Inventario;
import astore.productos.Producto;

/** Empleado con permisos especiales: puede cambiar precios y resurtir el inventario. */
public class Administrador extends Personal {
    private int nivelAcceso; // 1 = básico, 2 = medio, 3 = total

    public Administrador(String id, String nombreCompleto, String telefono, String horarioLaboral, int nivelAcceso) {
        super(id, nombreCompleto, telefono, "Administrador", horarioLaboral);
        if (nivelAcceso < 1 || nivelAcceso > 3) {
            throw new IllegalArgumentException("El nivel de acceso debe ser 1, 2 o 3.");
        }
        this.nivelAcceso = nivelAcceso;
    }

    public int getNivelAcceso() { return nivelAcceso; }

    /** Cambia el precio de un producto. Se necesita nivel 2 o mayor. */
    public boolean cambiarPrecio(Producto producto, double nuevoPrecio) {
        if (nivelAcceso < 2 || nuevoPrecio < 0) {
            return false;
        }
        producto.setPrecioLista(nuevoPrecio);
        producto.guardar();
        return true;
    }

    /** Agrega piezas al inventario. Se necesita nivel 2 o mayor. */
    public boolean reabastecer(Inventario inventario, int cantidad) {
        if (nivelAcceso < 2 || cantidad <= 0) {
            return false;
        }
        inventario.agregarStock(cantidad);
        return true;
    }

    @Override
    public String getTipo() { return "Administrador"; }

    @Override
    public String getNombreArchivo() { return "administradores"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"id", "nombre", "telefono", "horario", "nivel_acceso"};
    }

    @Override
    public String[] getValores() {
        return new String[]{getId(), getNombreCompleto(), getTelefono(), getHorarioLaboral(),
                String.valueOf(nivelAcceso)};
    }
}
