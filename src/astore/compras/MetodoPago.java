package astore.compras;

/**
 * Clase abstracta de los métodos de pago. Cada método (efectivo, tarjeta, credencial)
 * decide a su manera si acepta o no el pago (polimorfismo).
 */
public abstract class MetodoPago {
    private String nombre;

    public MetodoPago(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }

    /** Intenta cobrar el monto. Devuelve true si el pago fue aceptado. */
    public abstract boolean pagar(double monto);

    public String describir() {
        return "Método de pago: " + nombre;
    }
}
