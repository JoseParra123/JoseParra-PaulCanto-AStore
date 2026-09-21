public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA A-STORE - DEMOSTRACIÓN ===");

        // Instancias de personas
        Personal empleado = new Personal("EMP-01", "Carlos Gómez", "9981234567", "Cajero", "Matutino");
        clientes cliente = new clientes("CLI-100", "Ana Martínez", "9987654321", "0012345@anahuac.mx");

        // Instancias de productos (clase en minúscula)
        productos p1 = new productos("SKU-101", "Sudadera Anáhuac", 650.00);
        productos p2 = new productos("SKU-102", "Termo Metálico", 250.00);

        // Instancia de ventas (clase y método en minúscula)
        ventas nuevaventa = new ventas("VEN-001", empleado, cliente);
        nuevaventa.agregarproducto(p1);
        nuevaventa.agregarproducto(p2);

        // Imprimir resultados
        System.out.println("\n--- DETALLE DE LA VENTA ---");
        System.out.println("Folio: " + nuevaventa.getFolioVenta());
        System.out.println("Atendido por: " + nuevaventa.getVendedor().getNombreCompleto());
        System.out.println("Cliente: " + nuevaventa.getCliente().getNombreCompleto());
        System.out.println("Total a pagar: $" + nuevaventa.calcularTotal());
    }
}