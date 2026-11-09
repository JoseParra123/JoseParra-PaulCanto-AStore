import astore.Tienda;
import astore.compras.*;
import astore.personas.*;
import astore.productos.*;
import astore.util.GestorUsuario;
import java.time.LocalDate;
import java.util.List;


public class PruebasClases {
    static int correctas = 0;
    static int fallidas = 0;

    static void comprobar(boolean condicion, String descripcion) {
        if (condicion) {
            correctas++;
        } else {
            fallidas++;
            System.out.println("FALLA: " + descripcion);
        }
    }

    /** Comprueba que un bloque de código lance la excepción indicada. */
    static void lanza(Class<? extends Exception> tipo, String descripcion, Runnable codigo) {
        try {
            codigo.run();
            comprobar(false, descripcion + " (no lanzó excepción)");
        } catch (Exception e) {
            comprobar(tipo.isInstance(e), descripcion + " (lanzó " + e.getClass().getSimpleName() + ")");
        }
    }

    public static void main(String[] args) {
        // Las pruebas escriben archivos CSV; si ya hay datos reales no se deben mezclar.
        if (new java.io.File(GestorUsuario.CARPETA).exists()) {
            System.out.println("Ya existe la carpeta '" + GestorUsuario.CARPETA + "'. Ejecuta las pruebas desde una carpeta vacía");
            System.out.println("para no mezclar los datos de prueba con tus datos reales.");
            System.exit(2);
        }

        pruebasTienda(); // primero: necesita que la carpeta de datos esté vacía para crear los datos de demostración
        pruebasProductos();
        pruebasInventarioYAlmacen();
        pruebasProveedorYPromocion();
        pruebasPersonas();
        pruebasCarritoYPedido();
        pruebasPagosVentaCaja();
        pruebasGestorUsuario();

        System.out.println("Pruebas correctas: " + correctas + " | fallidas: " + fallidas);
        System.exit(fallidas == 0 ? 0 : 1);
    }

    static void pruebasProductos() {
        Categoria ropa = new Categoria("ropa", "Ropa", "Prendas");
        lanza(IllegalArgumentException.class, "Categoria sin nombre", () -> new Categoria("x", " ", ""));
        ropa.setNombre("");
        comprobar(ropa.getNombre().equals("Ropa"), "Categoria.setNombre vacío no cambia el nombre");

        Producto p = new Producto("SKU-1", "Playera", 200, "desc", ropa, "img.svg");
        comprobar(p.getCategoria() == ropa, "Producto guarda su categoría");
        comprobar(p.calcularPrecioConDescuento(10) == 180.0, "Producto: 10% de 200 = 180");
        p.setPrecioLista(-5);
        comprobar(p.getPrecioLista() == 200, "Producto.setPrecioLista negativo se ignora");
        lanza(IllegalArgumentException.class, "Producto con precio negativo", () -> new Producto("S", "N", -1));
        lanza(IllegalArgumentException.class, "Producto sin SKU", () -> new Producto("", "N", 1));
        comprobar(p.getValores()[4].equals("ropa"), "Producto.getValores incluye el id de la categoría");
    }

    static void pruebasInventarioYAlmacen() {
        Producto p = new Producto("SKU-2", "Termo", 250);
        Inventario inv = new Inventario(p, 5, 2, "A1");
        comprobar(inv.hayStock(5) && !inv.hayStock(6) && !inv.hayStock(0), "Inventario.hayStock");
        inv.descontarStock(2);
        comprobar(inv.getCantidadEnExhibicion() == 3, "Inventario.descontarStock resta");
        lanza(IllegalStateException.class, "Descontar más del stock", () -> inv.descontarStock(10));
        lanza(IllegalArgumentException.class, "Descontar cantidad 0", () -> inv.descontarStock(0));
        comprobar(!inv.alertarFaltante() && inv.solicitarResurtido() == null, "Sin alerta con 3 piezas (mínimo 2)");
        inv.descontarStock(1);
        comprobar(inv.alertarFaltante() && inv.solicitarResurtido().contains("Termo"), "Alerta al llegar al mínimo");
        inv.agregarStock(10);
        comprobar(inv.getCantidadEnExhibicion() == 12 && !inv.getUltimoResurtido().equals("Sin resurtido"), "Inventario.agregarStock");
        inv.registrarMerma(2);
        comprobar(inv.getCantidadEnExhibicion() == 10, "Inventario.registrarMerma");
        inv.ajustarConteoFisico(7);
        comprobar(inv.getCantidadEnExhibicion() == 7, "Inventario.ajustarConteoFisico");
        lanza(IllegalArgumentException.class, "Conteo negativo", () -> inv.ajustarConteoFisico(-1));
        lanza(IllegalArgumentException.class, "Inventario sin producto", () -> new Inventario(null, 1, 1, ""));

        Almacen alm = new Almacen("BOD-X", p, 10, "P1", "L1");
        alm.recibirEmbarque(5, "L2");
        comprobar(alm.getCantidadEnBodega() == 15 && alm.getLoteIngreso().equals("L2"), "Almacen.recibirEmbarque");
        comprobar(alm.transferirATienda(inv, 4) && alm.getCantidadEnBodega() == 11 && inv.getCantidadEnExhibicion() == 11,
                "Almacen.transferirATienda mueve piezas a la tienda");
        comprobar(!alm.transferirATienda(inv, 99), "Almacen.transferirATienda rechaza más de lo que hay");
        comprobar(alm.devolverAProveedor(1) && !alm.devolverAProveedor(500) && alm.getCantidadEnBodega() == 10, "Almacen.devolverAProveedor");
        comprobar(!alm.estaCaducado(LocalDate.now()), "Sin caducidad = no caduca");
        alm.setFechaCaducidad("2020-01-01");
        comprobar(alm.estaCaducado(LocalDate.now()), "Almacen.estaCaducado con fecha pasada");
        lanza(IllegalArgumentException.class, "Embarque de 0 piezas", () -> alm.recibirEmbarque(0, "L"));
    }

    static void pruebasProveedorYPromocion() {
        Proveedor prov = new Proveedor("PRV-X", "Empresa", "Contacto", "1", "a@b.c", "Ropa", 30);
        comprobar(prov.calcularFechaPago(LocalDate.of(2026, 1, 1)).equals(LocalDate.of(2026, 1, 31)), "Proveedor.calcularFechaPago");
        comprobar(prov.crearOrdenCompra(new Producto("S", "Gorra", 1), 5).contains("5 pza(s) de Gorra"), "Proveedor.crearOrdenCompra");
        lanza(IllegalArgumentException.class, "Orden con cantidad 0", () -> prov.crearOrdenCompra(new Producto("S", "G", 1), 0));
        prov.setDiasCredito(-1);
        comprobar(prov.getDiasCredito() == 30, "Proveedor.setDiasCredito negativo se ignora");
        lanza(IllegalArgumentException.class, "Proveedor sin razón social", () -> new Proveedor("P", "", "", "", "", "", 0));

        Promocion promo = new Promocion("test10", "Prueba", "regla", 10, LocalDate.now().plusDays(5), "Estudiantes");
        comprobar(promo.getCodigoPromocion().equals("TEST10"), "Promocion guarda el código en mayúsculas");
        comprobar(promo.estaVigente(LocalDate.now()) && !promo.estaVigente(LocalDate.now().plusDays(6)), "Promocion.estaVigente");
        comprobar(promo.aplicaPara("estudiantes") && !promo.aplicaPara("Todos"), "Promocion.aplicaPara por perfil");
        comprobar(promo.calcularDescuento(1550) == 155.0, "Promocion.calcularDescuento 10% de 1550");
        promo.desactivarPromocion();
        comprobar(!promo.estaVigente(LocalDate.now()), "Promocion desactivada ya no es vigente");
        lanza(IllegalArgumentException.class, "Descuento mayor a 100", () -> new Promocion("X", "n", "r", 101, LocalDate.now(), "Todos"));
    }

    static void pruebasPersonas() {
        lanza(IllegalArgumentException.class, "Cliente sin nombre", () -> new Cliente("C", " ", "1", "a@b.c"));
        lanza(IllegalArgumentException.class, "Cliente con correo inválido", () -> new Cliente("C", "Ana", "1", "sin-arroba"));
        Cliente c = new Cliente("CLI-T", "Ana", "1", "a@anahuac.mx");
        c.setPuntosLeones(-3);
        comprobar(c.getPuntosLeones() == 0, "Cliente.setPuntosLeones negativo se ignora");
        c.agregarPuntos(5);
        comprobar(c.getPuntosLeones() == 5, "Cliente.agregarPuntos");
        c.setCreditoInstitucional(100);
        comprobar(!c.usarCredito(150) && c.usarCredito(40) && c.getCreditoInstitucional() == 60, "Cliente.usarCredito");
        c.setCorreoInstitucional("malo");
        comprobar(c.getCorreoInstitucional().equals("a@anahuac.mx"), "Cliente.setCorreoInstitucional inválido se ignora");

        Personal emp = new Personal("EMP-T", "Luis", "2", "Cajero", "Matutino");
        emp.setPuestoRol("Vendedor");
        comprobar(emp.getPuestoRol().equals("Vendedor"), "Personal.setPuestoRol");

        // Polimorfismo: getTipo() depende de la clase real del objeto
        Persona[] personas = {c, emp, new Administrador("ADM-T", "Eva", "3", "Completo", 3)};
        comprobar(personas[0].getTipo().equals("Cliente") && personas[1].getTipo().equals("Empleado")
                && personas[2].getTipo().equals("Administrador"), "Polimorfismo: getTipo() de cada hija");
        comprobar(personas[2].describir().startsWith("Administrador:"), "Persona.describir usa getTipo()");

        Producto p = new Producto("SKU-3", "Mochila", 500);
        Inventario inv = new Inventario(p, 2, 1, "X");
        Administrador basico = new Administrador("ADM-1", "Basico", "4", "M", 1);
        Administrador total = new Administrador("ADM-2", "Total", "5", "M", 3);
        comprobar(!basico.cambiarPrecio(p, 400) && p.getPrecioLista() == 500, "Administrador nivel 1 no puede cambiar precios");
        comprobar(total.cambiarPrecio(p, 400) && p.getPrecioLista() == 400, "Administrador nivel 3 sí puede cambiar precios");
        comprobar(!basico.reabastecer(inv, 5) && total.reabastecer(inv, 5) && inv.getCantidadEnExhibicion() == 7, "Administrador.reabastecer según nivel");
        lanza(IllegalArgumentException.class, "Nivel de acceso 9", () -> new Administrador("A", "N", "1", "M", 9));
    }

    static void pruebasCarritoYPedido() {
        Producto a = new Producto("A", "Libreta", 75);
        Producto b = new Producto("B", "Pluma", 45);
        Carrito carrito = new Carrito();
        comprobar(carrito.estaVacio(), "Carrito nuevo está vacío");
        carrito.agregar(a, 2);
        carrito.agregar(b, 1);
        carrito.agregar(a, 1); // el mismo producto se suma, no se duplica
        comprobar(carrito.getItems().size() == 2 && carrito.contarArticulos() == 4, "Carrito junta productos repetidos");
        comprobar(carrito.calcularSubtotal() == 270.0, "Carrito.calcularSubtotal = 3x75 + 45");
        Promocion promo = new Promocion("P10", "p", "r", 10, LocalDate.now().plusDays(1), "Todos");
        comprobar(carrito.calcularDescuento(promo) == 27.0 && carrito.calcularTotal(promo) == 243.0, "Carrito: descuento y total con promoción");
        comprobar(carrito.calcularTotal(null) == 270.0, "Carrito.calcularTotal sin promoción");
        carrito.cambiarCantidad("B", 4);
        comprobar(carrito.calcularSubtotal() == 405.0, "Carrito.cambiarCantidad");
        carrito.cambiarCantidad("B", 0);
        comprobar(carrito.getItems().size() == 1, "Cantidad 0 elimina el producto");
        comprobar(carrito.eliminar("A") && !carrito.eliminar("A"), "Carrito.eliminar");
        carrito.agregar(a, 1);
        carrito.vaciar();
        comprobar(carrito.estaVacio(), "Carrito.vaciar");

        lanza(IllegalArgumentException.class, "ItemCarrito con cantidad 0", () -> new ItemCarrito(a, 0));
        lanza(IllegalArgumentException.class, "ItemCarrito sin producto", () -> new ItemCarrito(null, 1));
        lanza(IllegalArgumentException.class, "DetallePedido con cantidad 0", () -> new DetallePedido("A", "n", 1, 0));
        comprobar(new DetallePedido(new ItemCarrito(a, 3)).getSubtotal() == 225.0, "DetallePedido desde ItemCarrito");

        Pedido pedido = new Pedido("PED-T1", "2026-11-08 10:00", "Ana", "a@b.c", "Efectivo");
        pedido.agregarDetalle(new DetallePedido("A", "Libreta, grande", 75, 2));
        pedido.agregarDetalle(new DetallePedido("B", "Pluma", 45, 1));
        pedido.aplicarDescuento("P10", 19.5);
        comprobar(pedido.calcularSubtotal() == 195.0 && pedido.getTotal() == 175.5, "Pedido: subtotal y total");
        comprobar(pedido.getEstado().equals(Pedido.PENDIENTE), "Pedido nuevo está Pendiente");
        pedido.cambiarEstado(Pedido.PAGADO);
        comprobar(pedido.getEstado().equals("Pagado"), "Pedido.cambiarEstado");
        lanza(IllegalArgumentException.class, "Estado de pedido inválido", () -> pedido.cambiarEstado("Volando"));
        lanza(IllegalArgumentException.class, "Pedido sin nombre", () -> new Pedido("P", "f", " ", "", ""));
        // Guardar y recuperar el pedido desde su fila CSV
        Pedido copia = Pedido.desdeFila(pedido.getValores());
        comprobar(copia.getFolio().equals("PED-T1") && copia.getDetalles().size() == 2 && copia.getTotal() == 175.5
                && copia.getEstado().equals("Pagado"), "Pedido.desdeFila reconstruye el pedido");
    }

    static void pruebasPagosVentaCaja() {
        PagoEfectivo poco = new PagoEfectivo(50);
        comprobar(!poco.pagar(100), "PagoEfectivo rechaza si no alcanza");
        PagoEfectivo bien = new PagoEfectivo(200);
        comprobar(bien.pagar(150.5) && bien.getCambio() == 49.5, "PagoEfectivo calcula el cambio");
        comprobar(new PagoTarjeta("1234").pagar(10) && !new PagoTarjeta("12").pagar(10) && !new PagoTarjeta(null).pagar(10), "PagoTarjeta valida los 4 dígitos");
        Cliente c = new Cliente("CLI-P", "Beto", "1", "b@anahuac.mx");
        comprobar(!new PagoCredencial(c).pagar(10), "PagoCredencial sin saldo");
        c.setCreditoInstitucional(100);
        comprobar(new PagoCredencial(c).pagar(30) && c.getCreditoInstitucional() == 70, "PagoCredencial descuenta el saldo");
        comprobar(new PagoEfectivo(1).describir().equals("Método de pago: Efectivo"), "MetodoPago.describir");

        Personal cajero = new Personal("EMP-V", "Cajero", "1", "Cajero", "M");
        Producto p = new Producto("SKU-V", "Sudadera", 650);
        Venta venta = new Venta("VEN-T", cajero, c);
        lanza(IllegalStateException.class, "Cobrar venta vacía", () -> venta.cobrar(new PagoEfectivo(1000)));
        venta.agregarProducto(p, 2);
        comprobar(venta.calcularTotal() == 1300.0, "Venta.calcularTotal");
        comprobar(!venta.cobrar(new PagoEfectivo(100)) && !venta.isPagada(), "Venta no se cobra con pago insuficiente");
        comprobar(venta.cobrar(new PagoEfectivo(1500)) && venta.isPagada() && venta.getTotalCobrado() == 1300.0, "Venta.cobrar");
        comprobar(c.getPuntosLeones() == 13, "Venta suma 1 punto por cada $100");
        lanza(IllegalStateException.class, "Cobrar dos veces", () -> venta.cobrar(new PagoEfectivo(2000)));
        lanza(IllegalStateException.class, "Agregar producto a venta pagada", () -> venta.agregarProducto(p, 1));

        Terminal t = new Terminal("TER-T", "Caja 1", "Entrada");
        comprobar(t.imprimirTicket(venta).contains("TOTAL: $1300.00"), "Terminal.imprimirTicket");
        t.reportarFalla("sin papel");
        comprobar(!t.estaOperativa(), "Terminal con falla no está operativa");
        lanza(IllegalStateException.class, "Ticket en terminal con falla", () -> t.imprimirTicket(venta));
        comprobar(t.solicitarMantenimiento().contains("Caja 1") && t.getEstadoOperativo().equals(Terminal.EN_MANTENIMIENTO), "Terminal.solicitarMantenimiento");
        t.marcarOperativa();
        comprobar(t.estaOperativa(), "Terminal.marcarOperativa");

        CierreCaja corte = new CierreCaja("CORTE-T", cajero, t, 500);
        corte.registrarVenta(venta);
        comprobar(corte.getTotalEfectivo() == 1300.0 && corte.calcularEfectivoEsperado() == 1800.0, "CierreCaja suma efectivo");
        comprobar(corte.cerrarTurno(1780) == -20.0 && corte.generarReporte().contains("Faltante de $20.00"), "CierreCaja detecta faltante");
        lanza(IllegalStateException.class, "Registrar venta en turno cerrado", () -> corte.registrarVenta(venta));
        lanza(IllegalArgumentException.class, "Corte sin terminal", () -> new CierreCaja("C", cajero, null, 0));

        comprobar(Factura.validarRFC("XAXX010101000") && Factura.validarRFC("abc010101ab1") && !Factura.validarRFC("12345") && !Factura.validarRFC(null), "Factura.validarRFC");
        lanza(IllegalArgumentException.class, "Factura con RFC inválido", () -> new Factura(venta, "mal", "r", "d", "c", "G03"));
        Venta sinPagar = new Venta("VEN-NP", cajero, c);
        lanza(IllegalArgumentException.class, "Factura de venta no cobrada", () -> new Factura(sinPagar, "XAXX010101000", "r", "d", "c", "G03"));
        comprobar(new Factura(venta, "xaxx010101000", "Público", "Cancún", "a@b.c", "G03").getRfcCliente().equals("XAXX010101000"), "Factura guarda el RFC en mayúsculas");
    }

    static void pruebasGestorUsuario() {
        Categoria c = new Categoria("gu-1", "Con, coma \"y comillas\"", "desc");
        c.guardar();
        c.guardar(); // guardar dos veces actualiza, no duplica
        List<String[]> filas = GestorUsuario.cargar("categorias");
        long iguales = filas.stream().filter(f -> f[0].equals("gu-1")).count();
        comprobar(iguales == 1, "GestorUsuario.guardar no duplica filas");
        String[] fila = filas.stream().filter(f -> f[0].equals("gu-1")).findFirst().get();
        comprobar(fila[1].equals("Con, coma \"y comillas\""), "GestorUsuario guarda y lee comas y comillas");
        comprobar(GestorUsuario.cargar("no_existe").isEmpty(), "GestorUsuario.cargar de archivo inexistente = lista vacía");
        comprobar(!GestorUsuario.guardar(new Categoria("ok", "x", "")) == false, "GestorUsuario.guardar devuelve true");
    }

    static void pruebasTienda() {
        Tienda tienda = new Tienda();
        comprobar(tienda.getProductos().size() == 14 && tienda.getCategorias().size() == 5, "Tienda crea datos de demostración");
        comprobar(tienda.getProductosPorCategoria("papeleria").size() == 3, "Tienda.getProductosPorCategoria");
        comprobar(tienda.buscarProducto("NOEXISTE") == null && tienda.buscarProducto("SKU-101") != null, "Tienda.buscarProducto");
        lanza(IllegalArgumentException.class, "Promoción inexistente", () -> tienda.validarPromocion("NOPE", "Todos"));
        lanza(IllegalArgumentException.class, "Promoción de estudiantes para otro perfil", () -> tienda.validarPromocion("LEONES15", "Todos"));
        comprobar(tienda.validarPromocion("leones15", "Estudiantes").getPorcentajeDescuento() == 15, "Tienda.validarPromocion (sin importar mayúsculas)");

        // Un pedido que falla a la mitad NO debe descontar nada (todo o nada)
        int stockSudadera = tienda.buscarInventario("SKU-101").getCantidadEnExhibicion();
        java.util.ArrayList<ItemCarrito> items = new java.util.ArrayList<>();
        items.add(new ItemCarrito(tienda.buscarProducto("SKU-101"), 1));
        items.add(new ItemCarrito(tienda.buscarProducto("SKU-104"), 99)); // no hay 99 gorras
        lanza(IllegalStateException.class, "Pedido con producto sin stock", () -> tienda.crearPedido("Ana", "", "Efectivo", "", "Todos", items));
        comprobar(tienda.buscarInventario("SKU-101").getCantidadEnExhibicion() == stockSudadera && tienda.getPedidos().isEmpty(),
                "Pedido fallido no descuenta stock ni guarda el pedido");

        items.remove(1);
        Pedido pedido = tienda.crearPedido("Ana", "a@b.c", "Efectivo", "BIENVENIDA10", "Todos", items);
        comprobar(pedido.getTotal() == 585.0 && tienda.buscarInventario("SKU-101").getCantidadEnExhibicion() == stockSudadera - 1, "Tienda.crearPedido descuenta stock y aplica descuento");

        lanza(IllegalArgumentException.class, "Pedido con carrito vacío", () -> tienda.crearPedido("Ana", "", "Efectivo", "", "Todos", new java.util.ArrayList<>()));
        lanza(IllegalArgumentException.class, "Pedido sin nombre", () -> tienda.crearPedido(" ", "", "Efectivo", "", "Todos", items));

        tienda.actualizarProducto("SKU-106", 70, 11);
        comprobar(tienda.buscarProducto("SKU-106").getPrecioLista() == 70 && tienda.buscarInventario("SKU-106").getCantidadEnExhibicion() == 11, "Tienda.actualizarProducto");
        lanza(IllegalArgumentException.class, "actualizarProducto con precio negativo", () -> tienda.actualizarProducto("SKU-106", -1, 5));
        lanza(IllegalArgumentException.class, "actualizarProducto de SKU inexistente", () -> tienda.actualizarProducto("NOPE", 1, 1));

        // Venta de mostrador a través de la tienda
        Personal cajero = new Personal("EMP-TT", "Cajero", "1", "Cajero", "M");
        Venta venta = new Venta("VEN-TT", cajero, null);
        venta.agregarProducto(tienda.buscarProducto("SKU-108"), 2);
        int antes = tienda.buscarInventario("SKU-108").getCantidadEnExhibicion();
        comprobar(!tienda.registrarVenta(venta, new PagoEfectivo(10)) && tienda.buscarInventario("SKU-108").getCantidadEnExhibicion() == antes, "registrarVenta con pago rechazado no descuenta stock");
        comprobar(tienda.registrarVenta(venta, new PagoEfectivo(100)) && tienda.buscarInventario("SKU-108").getCantidadEnExhibicion() == antes - 2, "registrarVenta cobrada descuenta stock");

        // Persistencia: una Tienda nueva lee lo que quedó guardado
        Tienda otra = new Tienda();
        comprobar(otra.getPedidos().size() == 1 && otra.getPedidos().get(0).getTotal() == 585.0, "Persistencia: el pedido sobrevive al reiniciar");
        comprobar(otra.buscarProducto("SKU-106").getPrecioLista() == 70, "Persistencia: el precio editado sobrevive");
        comprobar(otra.buscarInventario("SKU-108").getCantidadEnExhibicion() == antes - 2, "Persistencia: el stock sobrevive");
        comprobar(otra.getBodega().size() == 3 && otra.getProveedores().size() == 2 && otra.getPromociones().size() == 2, "Persistencia: bodega, proveedores y promociones");
    }
}
