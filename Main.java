public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA A-STORE - DEMOSTRACIÓN ===");

        // cada una se guarda sola en la carpeta "usuario"
        Personal empleado = new Personal("EMP-01", "Carlos Gómez", "9981234567", "Cajero", "Matutino");
        clientes cliente = new clientes("CLI-100", "Ana Martínez", "9987654321", "0012345@anahuac.mx");

       
        productos p1 = new productos("SKU-101", "Sudadera Anáhuac", 650.00);
        productos p2 = new productos("SKU-102", "Termo Metálico", 250.00);

        
        ventas nuevaventa = new ventas("VEN-001", empleado, cliente);
        nuevaventa.agregarproducto(p1);
        nuevaventa.agregarproducto(p2);

        // Imprimir resultados
        System.out.println("\n--- DETALLE DE LA VENTA ---");
        System.out.println("Folio: " + nuevaventa.getFolioVenta());
        System.out.println("Atendido por: " + nuevaventa.getVendedor().getNombreCompleto());
        System.out.println("Cliente: " + nuevaventa.getCliente().getNombreCompleto());
        System.out.println("Total a pagar: $" + nuevaventa.calcularTotal());

        
        inventario inv = new inventario();
        inv.setProducto(p1.getCodigoSKU());
        inv.setCantidad_En_Exhibicion(12);
        inv.setStock_Minimo(3);
        inv.setUbicacion_Exhibidor("Pasillo 2 - Exhibidor A");
        inv.guardar();

        System.out.println("\nTodo quedó guardado en la carpeta '" + GestorUsuario.CARPETA + "'.");
    }
}