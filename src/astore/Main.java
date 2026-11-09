package astore;

import astore.compras.CierreCaja;
import astore.compras.Factura;
import astore.compras.MetodoPago;
import astore.compras.PagoCredencial;
import astore.compras.PagoEfectivo;
import astore.compras.PagoTarjeta;
import astore.compras.Terminal;
import astore.compras.Venta;
import astore.personas.Administrador;
import astore.personas.Cliente;
import astore.personas.Persona;
import astore.personas.Personal;
import astore.productos.Inventario;
import astore.productos.Producto;
import astore.util.GestorUsuario;
import astore.web.ServidorWeb;
import java.io.IOException;

/**
 * Punto de entrada del proyecto.
 *   java -cp out astore.Main          -> inicia la página web en http://localhost:8080
 *   java -cp out astore.Main 9090     -> inicia la página web en otro puerto
 *   java -cp out astore.Main demo     -> demostración en consola (ventas, caja, factura)
 */
public class Main {
    public static void main(String[] args) {
        Tienda tienda = new Tienda();

        if (args.length > 0 && args[0].equalsIgnoreCase("demo")) {
            demostracion(tienda);
            return;
        }

        int puerto = 8080;
        if (args.length > 0) {
            try {
                puerto = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("El puerto no es válido, se usará el 8080.");
            }
        }
        try {
            new ServidorWeb(tienda, puerto).iniciar();
        } catch (IOException e) {
            System.out.println("No se pudo iniciar el servidor (¿el puerto " + puerto + " está ocupado?): " + e.getMessage());
        }
    }

    /** Demostración en consola de las clases principales. */
    private static void demostracion(Tienda tienda) {
        System.out.println("=== SISTEMA A-STORE - DEMOSTRACIÓN ===");

        // Polimorfismo: todos son Persona, pero cada uno responde distinto a getTipo()
        Personal cajero = new Personal("EMP-01", "Carlos Gómez", "9981234567", "Cajero", "Matutino");
        Administrador admin = new Administrador("ADM-01", "Laura Pérez", "9980000000", "Completo", 3);
        Cliente cliente = new Cliente("CLI-100", "Ana Martínez", "9987654321", "0012345@anahuac.mx");
        Persona[] personas = {cajero, admin, cliente};
        System.out.println("\n--- PERSONAS ---");
        for (Persona p : personas) {
            System.out.println(p.describir());
        }

        // Venta de mostrador
        Producto sudadera = tienda.buscarProducto("SKU-101");
        Producto termo = tienda.buscarProducto("SKU-102");
        Venta venta = new Venta("VEN-001", cajero, cliente);
        venta.agregarProducto(sudadera, 1);
        venta.agregarProducto(termo, 2);

        System.out.println("\n--- DETALLE DE LA VENTA ---");
        System.out.println("Folio: " + venta.getFolioVenta());
        System.out.println("Atendido por: " + venta.getVendedor().getNombreCompleto());
        System.out.println("Cliente: " + venta.getCliente().getNombreCompleto());
        System.out.println("Total a pagar: $" + venta.calcularTotal());

        // Pago: una tarjeta inválida es rechazada, luego se paga en efectivo
        MetodoPago tarjetaMala = new PagoTarjeta("12");
        System.out.println("Pago con tarjeta inválida aceptado: " + tienda.registrarVenta(venta, tarjetaMala));
        PagoEfectivo efectivo = new PagoEfectivo(1500);
        if (tienda.registrarVenta(venta, efectivo)) {
            System.out.println("Pago en efectivo aceptado. Cambio: $" + efectivo.getCambio());
        }
        System.out.println("Puntos Leones de " + cliente.getNombreCompleto() + ": " + cliente.getPuntosLeones());

        // Crédito institucional: no alcanza el saldo
        Venta venta2 = new Venta("VEN-002", cajero, cliente);
        venta2.agregarProducto(tienda.buscarProducto("SKU-107"), 2);
        System.out.println("Pago con credencial (sin saldo) aceptado: " + tienda.registrarVenta(venta2, new PagoCredencial(cliente)));
        cliente.setCreditoInstitucional(500);
        System.out.println("Pago con credencial (con $500) aceptado: " + tienda.registrarVenta(venta2, new PagoCredencial(cliente)));

        // Ticket, corte de caja y factura
        Terminal terminal = new Terminal("TER-01", "Caja principal", "Entrada de la tienda");
        System.out.println("\n" + terminal.imprimirTicket(venta));

        CierreCaja corte = new CierreCaja("CORTE-001", cajero, terminal, 500);
        corte.registrarVenta(venta);
        corte.registrarVenta(venta2);
        corte.cerrarTurno(corte.calcularEfectivoEsperado() - 20);
        System.out.println(corte.generarReporte());

        Factura factura = new Factura(venta, "XAXX010101000", "Público en general", "Cancún, Q. Roo",
                "0012345@anahuac.mx", "G03");
        System.out.println("\n" + factura.generarResumen());

        // Manejo de excepciones: pedir más piezas de las que hay
        Inventario inv = tienda.buscarInventario("SKU-104");
        try {
            inv.descontarStock(999);
        } catch (IllegalStateException e) {
            System.out.println("\nExcepción controlada: " + e.getMessage());
        }
        String aviso = inv.solicitarResurtido();
        if (aviso != null) {
            System.out.println("Alerta de inventario: " + aviso);
        }

        System.out.println("\nTodo quedó guardado en la carpeta '" + GestorUsuario.CARPETA + "'.");
    }
}
