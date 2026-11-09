package astore.web;

import astore.Tienda;
import astore.compras.DetallePedido;
import astore.compras.ItemCarrito;
import astore.compras.Pedido;
import astore.productos.Categoria;
import astore.productos.Inventario;
import astore.productos.Producto;
import astore.productos.Promocion;
import astore.util.GestorUsuario;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Servidor web sencillo que viene incluido en Java (com.sun.net.httpserver).
 * Hace dos cosas: entrega los archivos de la carpeta "web" y responde la API en formato JSON.
 * Solo escucha en 127.0.0.1, es decir, únicamente se puede abrir desde la misma computadora.
 */
public class ServidorWeb {
    private Tienda tienda;
    private int puerto;
    private Path carpetaWeb;
    private HttpServer servidor;

    /** Cada ruta de la API se programa con una función que puede lanzar excepciones. */
    private interface Manejador {
        void manejar(HttpExchange ex) throws Exception;
    }

    public ServidorWeb(Tienda tienda, int puerto) {
        this.tienda = tienda;
        this.puerto = puerto;
        this.carpetaWeb = Paths.get("web").toAbsolutePath().normalize();
    }

    public void iniciar() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", puerto), 0);
        servidor.createContext("/api/productos", seguro(this::listarProductos));
        servidor.createContext("/api/categorias", seguro(this::listarCategorias));
        servidor.createContext("/api/promocion", seguro(this::consultarPromocion));
        servidor.createContext("/api/pedidos", seguro(this::crearPedido));
        servidor.createContext("/api/admin/pedidos", seguro(this::listarPedidos));
        servidor.createContext("/api/admin/actualizar", seguro(this::actualizarProducto));
        servidor.createContext("/", seguro(this::servirArchivo));
        servidor.start();
        System.out.println("A-Store está funcionando en http://localhost:" + puerto);
        System.out.println("Sirviendo la carpeta: " + carpetaWeb);
        System.out.println("Para detener el servidor presiona Ctrl + C.");
    }

    // ---------- Rutas de la API ----------

    private void listarProductos(HttpExchange ex) throws IOException {
        soloMetodo(ex, "GET");
        StringBuilder json = new StringBuilder("[");
        boolean primero = true;
        for (Producto p : tienda.getProductos()) {
            Inventario inv = tienda.buscarInventario(p.getCodigoSKU());
            int stock = (inv == null) ? 0 : inv.getCantidadEnExhibicion();
            boolean pocas = inv != null && inv.alertarFaltante() && stock > 0;
            Categoria c = p.getCategoria();
            if (!primero) json.append(',');
            primero = false;
            json.append("{\"sku\":").append(texto(p.getCodigoSKU()))
                .append(",\"nombre\":").append(texto(p.getNombreArticulo()))
                .append(",\"descripcion\":").append(texto(p.getDescripcion()))
                .append(",\"precio\":").append(p.getPrecioLista())
                .append(",\"categoriaId\":").append(texto(c == null ? "" : c.getId()))
                .append(",\"categoria\":").append(texto(c == null ? "" : c.getNombre()))
                .append(",\"imagen\":").append(texto(p.getImagen()))
                .append(",\"stock\":").append(stock)
                .append(",\"disponible\":").append(stock > 0)
                .append(",\"pocasPiezas\":").append(pocas)
                .append('}');
        }
        json.append(']');
        responderJson(ex, 200, json.toString());
    }

    private void listarCategorias(HttpExchange ex) throws IOException {
        soloMetodo(ex, "GET");
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tienda.getCategorias().size(); i++) {
            Categoria c = tienda.getCategorias().get(i);
            if (i > 0) json.append(',');
            json.append("{\"id\":").append(texto(c.getId()))
                .append(",\"nombre\":").append(texto(c.getNombre()))
                .append(",\"descripcion\":").append(texto(c.getDescripcion()))
                .append(",\"total\":").append(tienda.getProductosPorCategoria(c.getId()).size())
                .append('}');
        }
        json.append(']');
        responderJson(ex, 200, json.toString());
    }

    private void consultarPromocion(HttpExchange ex) throws IOException {
        soloMetodo(ex, "GET");
        Map<String, String> datos = parametros(ex.getRequestURI().getRawQuery());
        try {
            Promocion promo = tienda.validarPromocion(datos.getOrDefault("codigo", ""),
                    datos.getOrDefault("perfil", "Todos"));
            responderJson(ex, 200, "{\"valida\":true,\"nombre\":" + texto(promo.getNombreCampana())
                    + ",\"porcentaje\":" + promo.getPorcentajeDescuento() + "}");
        } catch (IllegalArgumentException e) {
            responderJson(ex, 200, "{\"valida\":false,\"mensaje\":" + texto(e.getMessage()) + "}");
        }
    }

    private void crearPedido(HttpExchange ex) throws IOException {
        soloMetodo(ex, "POST");
        Map<String, String> datos = parametros(leerCuerpo(ex));

        // Los productos llegan así: items=SKU-101:2,SKU-102:1
        ArrayList<ItemCarrito> items = new ArrayList<>();
        String textoItems = datos.getOrDefault("items", "");
        if (!textoItems.isEmpty()) {
            for (String par : textoItems.split(",")) {
                String[] partes = par.split(":");
                if (partes.length != 2) {
                    throw new IllegalArgumentException("Formato de productos no válido.");
                }
                Producto producto = tienda.buscarProducto(partes[0]);
                if (producto == null) {
                    throw new IllegalArgumentException("No existe el producto " + partes[0]);
                }
                items.add(new ItemCarrito(producto, Integer.parseInt(partes[1])));
            }
        }

        Pedido pedido = tienda.crearPedido(datos.getOrDefault("nombre", ""), datos.getOrDefault("correo", ""),
                datos.getOrDefault("metodo", "Efectivo"), datos.getOrDefault("codigo", ""),
                datos.getOrDefault("perfil", "Todos"), items);
        responderJson(ex, 200, "{\"ok\":true,\"folio\":" + texto(pedido.getFolio())
                + ",\"subtotal\":" + pedido.calcularSubtotal()
                + ",\"descuento\":" + pedido.getDescuento()
                + ",\"total\":" + pedido.getTotal() + "}");
    }

    private void listarPedidos(HttpExchange ex) throws IOException {
        soloMetodo(ex, "GET");
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tienda.getPedidos().size(); i++) {
            Pedido p = tienda.getPedidos().get(i);
            StringBuilder resumen = new StringBuilder();
            for (DetallePedido d : p.getDetalles()) {
                if (resumen.length() > 0) resumen.append(", ");
                resumen.append(d.getCantidad()).append(" x ").append(d.getNombreProducto());
            }
            if (i > 0) json.append(',');
            json.append("{\"folio\":").append(texto(p.getFolio()))
                .append(",\"fecha\":").append(texto(p.getFecha()))
                .append(",\"cliente\":").append(texto(p.getNombreCliente()))
                .append(",\"metodo\":").append(texto(p.getMetodoPago()))
                .append(",\"estado\":").append(texto(p.getEstado()))
                .append(",\"total\":").append(p.getTotal())
                .append(",\"detalle\":").append(texto(resumen.toString()))
                .append('}');
        }
        json.append(']');
        responderJson(ex, 200, json.toString());
    }

    private void actualizarProducto(HttpExchange ex) throws IOException {
        soloMetodo(ex, "POST");
        Map<String, String> datos = parametros(leerCuerpo(ex));
        try {
            tienda.actualizarProducto(datos.getOrDefault("sku", ""),
                    Double.parseDouble(datos.getOrDefault("precio", "")),
                    Integer.parseInt(datos.getOrDefault("stock", "")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio y el stock deben ser números.");
        }
        responderJson(ex, 200, "{\"ok\":true}");
    }

    // ---------- Archivos estáticos (HTML, CSS, JS, imágenes) ----------

    private void servirArchivo(HttpExchange ex) throws IOException {
        soloMetodo(ex, "GET");
        String ruta = ex.getRequestURI().getPath();
        if (ruta.equals("/")) {
            ruta = "/index.html";
        }
        Path archivo = carpetaWeb.resolve(ruta.substring(1)).normalize();
        // Seguridad: no permitir salir de la carpeta "web" con rutas como ../
        if (!archivo.startsWith(carpetaWeb) || !Files.isRegularFile(archivo)) {
            responder(ex, 404, "text/plain; charset=utf-8", "No encontrado: " + ruta);
            return;
        }
        byte[] datos = Files.readAllBytes(archivo);
        ex.getResponseHeaders().set("Content-Type", tipoContenido(archivo.toString()));
        ex.getResponseHeaders().set("Cache-Control", "no-cache");
        ex.sendResponseHeaders(200, datos.length);
        ex.getResponseBody().write(datos);
    }

    // ---------- Métodos de apoyo ----------

    /** Envuelve una ruta para convertir las excepciones en respuestas JSON con el mensaje de error. */
    private HttpHandler seguro(Manejador manejador) {
        return ex -> {
            try {
                manejador.manejar(ex);
            } catch (IllegalArgumentException | IllegalStateException e) {
                responderJson(ex, 400, "{\"ok\":false,\"error\":" + texto(e.getMessage()) + "}");
            } catch (Exception e) {
                GestorUsuario.error("Error en el servidor: " + e);
                responderJson(ex, 500, "{\"ok\":false,\"error\":\"Error interno del servidor.\"}");
            } finally {
                ex.close();
            }
        };
    }

    private void soloMetodo(HttpExchange ex, String metodo) {
        if (!ex.getRequestMethod().equalsIgnoreCase(metodo)) {
            throw new IllegalArgumentException("Método no permitido: usa " + metodo);
        }
    }

    private String leerCuerpo(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    /** Convierte "a=1&b=2" en un mapa {a: 1, b: 2}. */
    private Map<String, String> parametros(String consulta) {
        Map<String, String> mapa = new HashMap<>();
        if (consulta == null || consulta.isEmpty()) {
            return mapa;
        }
        for (String par : consulta.split("&")) {
            int igual = par.indexOf('=');
            String clave = (igual < 0) ? par : par.substring(0, igual);
            String valor = (igual < 0) ? "" : par.substring(igual + 1);
            mapa.put(URLDecoder.decode(clave, StandardCharsets.UTF_8),
                    URLDecoder.decode(valor, StandardCharsets.UTF_8));
        }
        return mapa;
    }

    /** Convierte un texto en un texto JSON entre comillas (escapando comillas y saltos de línea). */
    private String texto(String valor) {
        if (valor == null) {
            return "\"\"";
        }
        StringBuilder sb = new StringBuilder("\"");
        for (char c : valor.toCharArray()) {
            if (c == '"') sb.append("\\\"");
            else if (c == '\\') sb.append("\\\\");
            else if (c == '\n') sb.append("\\n");
            else if (c == '\r') sb.append("\\r");
            else if (c == '\t') sb.append("\\t");
            else sb.append(c);
        }
        return sb.append('"').toString();
    }

    private void responderJson(HttpExchange ex, int codigo, String json) throws IOException {
        responder(ex, codigo, "application/json; charset=utf-8", json);
    }

    private void responder(HttpExchange ex, int codigo, String tipo, String cuerpo) throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", tipo);
        ex.sendResponseHeaders(codigo, bytes.length);
        ex.getResponseBody().write(bytes);
    }

    private String tipoContenido(String nombre) {
        String n = nombre.toLowerCase();
        if (n.endsWith(".html")) return "text/html; charset=utf-8";
        if (n.endsWith(".css")) return "text/css; charset=utf-8";
        if (n.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (n.endsWith(".svg")) return "image/svg+xml";
        if (n.endsWith(".png")) return "image/png";
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
        if (n.endsWith(".json")) return "application/json; charset=utf-8";
        return "application/octet-stream";
    }
}
