import java.time.LocalDate;
import java.util.Scanner;

public class Promociones {

    private String codigoPromocion;
    private String nombreCampana;
    private String descripcionRegla;
    private double porcentajeDescuento;
    private LocalDate vigencia;
    private String perfilExclusivo;
    private boolean activa;

    public Promociones() {
        this.activa = true;
    }

    public void definirNombreCampana(String nombre) {
        this.nombreCampana = nombre;
    }

    public void configurarReglas(String codigo, String descripcion, double porcentaje, LocalDate fechaLimite, String perfil) {
        this.codigoPromocion = codigo;
        this.descripcionRegla = descripcion;
        this.porcentajeDescuento = porcentaje;
        this.vigencia = fechaLimite;
        this.perfilExclusivo = perfil;
    }

    public boolean validarVigencia() {
        if (!activa || vigencia == null) {
            return false;
        }
        return LocalDate.now().isBefore(vigencia) || LocalDate.now().isEqual(vigencia);
    }

    public double calcularDescuento(double precioOriginal) {
        if (validarVigencia()) {
            return precioOriginal - (precioOriginal * (porcentajeDescuento / 100.0));
        }
        return precioOriginal;
    }

    public void desactivarPromocion() {
        this.activa = false;
        System.out.println("La promoción ha sido desactivada.");
    }

    public void evaluarImpactoVentas(int cantidadVendida) {
        if (cantidadVendida > 50) {
            System.out.println("Impacto positivo: Buena recepción de la campaña.");
        } else if (cantidadVendida > 0) {
            System.out.println("Impacto moderado.");
        } else {
            System.out.println("Impacto nulo: No se registraron ventas.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Promociones promo = new Promociones();

        System.out.println("--- SISTEMA DE PROMOCIONES ---");
        
        System.out.print("Ingresa el código de la promoción (ej. DESC10): ");
        String codigo = scanner.nextLine();

        System.out.print("Ingresa el nombre de la campaña: ");
        String nombre = scanner.nextLine();
        promo.definirNombreCampana(nombre);

        System.out.print("Ingresa la regla de la promoción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ingresa el porcentaje de descuento (solo el número, ej. 20): ");
        double porcentaje = scanner.nextDouble();

        System.out.println("Vigencia de la promoción:");
        System.out.print("  Año (ej. 2026): ");
        int anio = scanner.nextInt();
        System.out.print("  Mes (1-12): ");
        int mes = scanner.nextInt();
        System.out.print("  Día (1-31): ");
        int dia = scanner.nextInt();
        scanner.nextLine(); // Limpiar el salto de línea

        System.out.print("Ingresa el perfil exclusivo (ej. Cliente Frecuente): ");
        String perfil = scanner.nextLine();

        LocalDate fecha = LocalDate.of(anio, mes, dia);
        promo.configurarReglas(codigo, descripcion, porcentaje, fecha, perfil);

        System.out.println("\n--- DATOS GUARDADOS CORRECTAMENTE ---");
        System.out.println("Promoción vigente: " + promo.validarVigencia());

        System.out.print("\nIngresa el precio de un producto para probar el cálculo: $");
        double precio = scanner.nextDouble();
        System.out.println("Precio final con descuento aplicado: $" + promo.calcularDescuento(precio));

        System.out.print("\nIngresa las ventas generadas por esta promo para evaluar: ");
        int ventas = scanner.nextInt();
        promo.evaluarImpactoVentas(ventas);

        scanner.close();
    }
}