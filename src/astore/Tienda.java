package astore;

import astore.compras.Carrito;
import astore.compras.DetallePedido;
import astore.compras.ItemCarrito;
import astore.compras.MetodoPago;
import astore.compras.Pedido;
import astore.compras.Venta;
import astore.productos.Almacen;
import astore.productos.Categoria;
import astore.productos.Inventario;
import astore.productos.Producto;
import astore.productos.Promocion;
import astore.productos.Proveedor;
import astore.util.GestorUsuario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase central de A-Store: guarda las listas (ArrayList) de categorías, productos, inventario,
 * promociones y pedidos, y coordina las operaciones principales.
 * Los datos se leen de la carpeta "datos". Si todavía no existe, se crean datos de demostración.
 */
public final class Tienda {
    private String nombre;
    private ArrayList<Categoria> categorias = new ArrayList<>();
    private ArrayList<Producto> productos = new ArrayList<>();
    private ArrayList<Inventario> inventarios = new ArrayList<>();
    private ArrayList<Almacen> bodega = new ArrayList<>();
    private ArrayList<Proveedor> proveedores = new ArrayList<>();
    private ArrayList<Promocion> promociones = new ArrayList<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public Tienda() {
        this.nombre = "A-Store Anáhuac Cancún";
        cargarDatos();
    }

    // ---------- Consultas ----------

    public String getNombre() { return nombre; }
    public ArrayList<Categoria> getCategorias() { return categorias; }
    public ArrayList<Producto> getProductos() { return productos; }
    public ArrayList<Inventario> getInventarios() { return inventarios; }
    public ArrayList<Almacen> getBodega() { return bodega; }
    public ArrayList<Proveedor> getProveedores() { return proveedores; }
    public ArrayList<Promocion> getPromociones() { return promociones; }
    public ArrayList<Pedido> getPedidos() { return pedidos; }

    public Categoria buscarCategoria(String id) {
        for (Categoria c : categorias) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public Producto buscarProducto(String sku) {
        for (Producto p : productos) {
            if (p.getCodigoSKU().equals(sku)) return p;
        }
        return null;
    }

    public Inventario buscarInventario(String sku) {
        for (Inventario i : inventarios) {
            if (i.getProducto().getCodigoSKU().equals(sku)) return i;
        }
        return null;
    }

    public Promocion buscarPromocion(String codigo) {
        for (Promocion p : promociones) {
            if (p.getCodigoPromocion().equalsIgnoreCase(codigo)) return p;
        }
        return null;
    }

    /** Devuelve los productos de una categoría. */
    public ArrayList<Producto> getProductosPorCategoria(String idCategoria) {
        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria() != null && p.getCategoria().getId().equals(idCategoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /** Busca una promoción y revisa que se pueda usar. Lanza IllegalArgumentException si no. */
    public Promocion validarPromocion(String codigo, String perfil) {
        Promocion promo = buscarPromocion(codigo);
        if (promo == null) {
            throw new IllegalArgumentException("El código de promoción no existe.");
        }
        if (!promo.estaVigente(LocalDate.now())) {
            throw new IllegalArgumentException("La promoción ya no está vigente.");
        }
        if (!promo.aplicaPara(perfil)) {
            throw new IllegalArgumentException("La promoción es exclusiva para: " + promo.getPerfilExclusivo());
        }
        return promo;
    }

    // ---------- Operaciones ----------

    /**
     * Crea un pedido desde la página web. Primero revisa todo (stock, promoción) y solo si
     * todo está bien descuenta el inventario. Si algo falla lanza una excepción y no cambia nada.
     */
    public Pedido crearPedido(String nombreCliente, String correo, String metodoPago,
                              String codigoPromocion, String perfil, ArrayList<ItemCarrito> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }

        // 1. Revisar stock de cada producto
        Carrito carrito = new Carrito();
        for (ItemCarrito item : items) {
            Inventario inv = buscarInventario(item.getProducto().getCodigoSKU());
            if (inv == null || !inv.hayStock(item.getCantidad())) {
                throw new IllegalStateException("No hay suficiente stock de " + item.getProducto().getNombreArticulo());
            }
            carrito.agregar(item.getProducto(), item.getCantidad());
        }

        // 2. Revisar la promoción (si hay)
        Promocion promo = null;
        if (codigoPromocion != null && !codigoPromocion.trim().isEmpty()) {
            promo = validarPromocion(codigoPromocion.trim(), perfil);
        }

        // 3. Crear el pedido (si el nombre está vacío, el constructor lanza una excepción)
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Pedido pedido = new Pedido(generarFolio(), fecha, nombreCliente, correo, metodoPago);
        for (ItemCarrito item : carrito.getItems()) {
            pedido.agregarDetalle(new DetallePedido(item));
        }
        pedido.aplicarDescuento(promo == null ? "" : promo.getCodigoPromocion(), carrito.calcularDescuento(promo));

        // 4. Todo bien: descontar inventario y guardar
        for (ItemCarrito item : carrito.getItems()) {
            buscarInventario(item.getProducto().getCodigoSKU()).descontarStock(item.getCantidad());
        }
        pedidos.add(pedido);
        pedido.guardar();
        return pedido;
    }

    /** Cobra una venta de mostrador con un método de pago y descuenta el inventario si se pagó. */
    public boolean registrarVenta(Venta venta, MetodoPago metodo) {
        for (ItemCarrito item : venta.getCarrito().getItems()) {
            Inventario inv = buscarInventario(item.getProducto().getCodigoSKU());
            if (inv == null || !inv.hayStock(item.getCantidad())) {
                throw new IllegalStateException("No hay suficiente stock de " + item.getProducto().getNombreArticulo());
            }
        }
        if (!venta.cobrar(metodo)) {
            return false;
        }
        for (ItemCarrito item : venta.getCarrito().getItems()) {
            buscarInventario(item.getProducto().getCodigoSKU()).descontarStock(item.getCantidad());
        }
        return true;
    }

    /** Usado por el panel de administración: cambia el precio y las existencias de un producto. */
    public void actualizarProducto(String sku, double nuevoPrecio, int nuevoStock) {
        Producto producto = buscarProducto(sku);
        Inventario inventario = buscarInventario(sku);
        if (producto == null || inventario == null) {
            throw new IllegalArgumentException("No existe el producto " + sku);
        }
        if (nuevoPrecio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        producto.setPrecioLista(nuevoPrecio);
        producto.guardar();
        inventario.ajustarConteoFisico(nuevoStock);
    }

    private String generarFolio() {
        return "PED-" + String.format("%04d", pedidos.size() + 1);
    }

    // ---------- Carga y guardado de datos ----------

    private void cargarDatos() {
        List<String[]> filasCategorias = GestorUsuario.cargar("categorias");
        if (filasCategorias.isEmpty()) {
            crearDatosDemostracion();
            return;
        }
        for (String[] f : filasCategorias) {
            try { categorias.add(new Categoria(f[0], f[1], f[2])); }
            catch (RuntimeException e) { GestorUsuario.error("Fila de categorías inválida: " + e.getMessage()); }
        }
        for (String[] f : GestorUsuario.cargar("productos")) {
            try { productos.add(new Producto(f[0], f[1], Double.parseDouble(f[2]), f[3], buscarCategoria(f[4]), f[5])); }
            catch (RuntimeException e) { GestorUsuario.error("Fila de productos inválida: " + e.getMessage()); }
        }
        for (String[] f : GestorUsuario.cargar("inventario")) {
            try {
                Inventario inv = new Inventario(buscarProducto(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]), f[3]);
                inv.setUltimoResurtido(f[4]);
                inventarios.add(inv);
            } catch (RuntimeException e) { GestorUsuario.error("Fila de inventario inválida: " + e.getMessage()); }
        }
        for (String[] f : GestorUsuario.cargar("almacen")) {
            try { bodega.add(new Almacen(f[0], buscarProducto(f[1]), Integer.parseInt(f[2]), f[3], f[4])); }
            catch (RuntimeException e) { GestorUsuario.error("Fila de almacén inválida: " + e.getMessage()); }
        }
        for (String[] f : GestorUsuario.cargar("proveedores")) {
            try { proveedores.add(new Proveedor(f[0], f[1], f[2], f[3], f[4], f[5], Integer.parseInt(f[6]))); }
            catch (RuntimeException e) { GestorUsuario.error("Fila de proveedores inválida: " + e.getMessage()); }
        }
        for (String[] f : GestorUsuario.cargar("promociones")) {
            try {
                Promocion promo = new Promocion(f[0], f[1], f[2], Double.parseDouble(f[3]), LocalDate.parse(f[4]), f[5]);
                promo.setActiva(Boolean.parseBoolean(f[6]));
                promociones.add(promo);
            } catch (RuntimeException e) { GestorUsuario.error("Fila de promociones inválida: " + e.getMessage()); }
        }
        for (String[] f : GestorUsuario.cargar("pedidos")) {
            try { pedidos.add(Pedido.desdeFila(f)); }
            catch (RuntimeException e) { GestorUsuario.error("Fila de pedidos inválida: " + e.getMessage()); }
        }
    }

    /** Datos ficticios para que la tienda funcione la primera vez. Precios e imágenes son de demostración. */
    private void crearDatosDemostracion() {
        Categoria ropa = new Categoria("ropa", "Ropa", "Playeras, sudaderas y gorras");
        Categoria accesorios = new Categoria("accesorios", "Accesorios", "Termos, mochilas y llaveros");
        Categoria papeleria = new Categoria("papeleria", "Papelería", "Libretas, plumas y carpetas");
        Categoria universitarios = new Categoria("universitarios", "Artículos universitarios", "Tazas, banderines y calcomanías");
        Categoria otros = new Categoria("otros", "Otros productos", "Paraguas y más");
        categorias.add(ropa);
        categorias.add(accesorios);
        categorias.add(papeleria);
        categorias.add(universitarios);
        categorias.add(otros);

        agregarProductoDemo("SKU-101", "Sudadera universitaria", 650.00, "Sudadera con capucha de algodón, color gris Oxford.", ropa, 12, "Pasillo 2 - Exhibidor A");
        agregarProductoDemo("SKU-102", "Termo metálico", 250.00, "Termo de acero inoxidable de 500 ml que conserva la temperatura.", accesorios, 20, "Pasillo 1 - Exhibidor C");
        agregarProductoDemo("SKU-103", "Playera universitaria", 280.00, "Playera de algodón con diseño de demostración.", ropa, 30, "Pasillo 2 - Exhibidor B");
        agregarProductoDemo("SKU-104", "Gorra deportiva", 220.00, "Gorra ajustable de color rojo oscuro.", ropa, 3, "Pasillo 2 - Exhibidor C");
        agregarProductoDemo("SKU-105", "Mochila universitaria", 520.00, "Mochila resistente con compartimento para laptop de 15 pulgadas.", accesorios, 8, "Pasillo 1 - Exhibidor A");
        agregarProductoDemo("SKU-106", "Llavero metálico", 60.00, "Llavero de metal grabado.", accesorios, 40, "Caja - Mostrador");
        agregarProductoDemo("SKU-107", "Libreta profesional", 75.00, "Libreta de 100 hojas con pasta dura, rayas.", papeleria, 25, "Pasillo 3 - Exhibidor A");
        agregarProductoDemo("SKU-108", "Plumas azules (paquete de 3)", 45.00, "Paquete de tres plumas de tinta azul.", papeleria, 50, "Pasillo 3 - Exhibidor B");
        agregarProductoDemo("SKU-109", "Carpeta con broche", 55.00, "Carpeta tamaño carta con broche metálico.", papeleria, 18, "Pasillo 3 - Exhibidor C");
        agregarProductoDemo("SKU-110", "Taza de cerámica", 120.00, "Taza blanca de 350 ml con diseño de demostración.", universitarios, 15, "Pasillo 4 - Exhibidor A");
        agregarProductoDemo("SKU-111", "Banderín de felpa", 90.00, "Banderín decorativo para cuarto o escritorio.", universitarios, 0, "Pasillo 4 - Exhibidor B");
        agregarProductoDemo("SKU-112", "Calcomanías (paquete de 5)", 35.00, "Paquete de cinco calcomanías resistentes al agua.", universitarios, 60, "Caja - Mostrador");
        agregarProductoDemo("SKU-113", "Paraguas plegable", 190.00, "Paraguas compacto, ideal para la temporada de lluvias.", otros, 10, "Pasillo 5 - Exhibidor A");
        agregarProductoDemo("SKU-114", "Tarjetero", 85.00, "Tarjetero de plástico para credencial y tarjetas.", otros, 22, "Pasillo 5 - Exhibidor B");

        bodega.add(new Almacen("BOD-001", buscarProducto("SKU-101"), 40, "Bodega - Pasillo A", "LOTE-2026-01"));
        bodega.add(new Almacen("BOD-002", buscarProducto("SKU-104"), 25, "Bodega - Pasillo A", "LOTE-2026-02"));
        bodega.add(new Almacen("BOD-003", buscarProducto("SKU-111"), 30, "Bodega - Pasillo B", "LOTE-2026-03"));

        proveedores.add(new Proveedor("PRV-001", "Textiles de Ejemplo S.A. de C.V.", "Contacto Ficticio",
                "0000000000", "contacto@ejemplo.invalid", "Ropa y gorras", 30));
        proveedores.add(new Proveedor("PRV-002", "Papelera de Ejemplo S.A. de C.V.", "Contacto Ficticio",
                "0000000000", "ventas@ejemplo.invalid", "Papelería", 15));

        promociones.add(new Promocion("BIENVENIDA10", "Bienvenida", "10% de descuento en tu pedido",
                10, LocalDate.of(2027, 6, 30), "Todos"));
        promociones.add(new Promocion("LEONES15", "Comunidad universitaria", "15% de descuento para estudiantes",
                15, LocalDate.of(2027, 6, 30), "Estudiantes"));

        guardarTodo();
    }

    private void agregarProductoDemo(String sku, String nombre, double precio, String descripcion,
                                     Categoria categoria, int stock, String ubicacion) {
        Producto producto = new Producto(sku, nombre, precio, descripcion, categoria, "img/" + sku.toLowerCase() + ".svg");
        Inventario inventario = new Inventario(producto, stock, 3, ubicacion);
        inventario.setUltimoResurtido("2026-11-01");
        productos.add(producto);
        inventarios.add(inventario);
    }

    /** Guarda todas las listas en sus archivos CSV. */
    public void guardarTodo() {
        for (Categoria c : categorias) c.guardar();
        for (Producto p : productos) p.guardar();
        for (Inventario i : inventarios) i.guardar();
        for (Almacen a : bodega) a.guardar();
        for (Proveedor p : proveedores) p.guardar();
        for (Promocion p : promociones) p.guardar();
        for (Pedido p : pedidos) p.guardar();
    }
}
