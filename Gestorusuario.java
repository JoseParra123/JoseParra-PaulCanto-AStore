import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public final class GestorUsuario {

    
    public static final String CARPETA = "usuario";

    private static final String BOM = "\uFEFF"; 
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private GestorUsuario() {}

    
    public static synchronized boolean guardar(Guardable objeto) {
        String[] valores = objeto.getValores();
        String nombre = objeto.getNombreArchivo();

        if (valores == null || valores.length == 0
                || valores[0] == null || valores[0].trim().isEmpty()) {
            registrar("OMITIDO " + nombre + ": el registro no tiene clave (primer campo vacío)");
            return false;
        }

        Path archivo = ruta(nombre + ".csv");
        try {
            Files.createDirectories(archivo.getParent());

            List<String> lineas = new ArrayList<>();
            if (Files.exists(archivo)) {
                lineas.addAll(Files.readAllLines(archivo, StandardCharsets.UTF_8));
            }
            if (lineas.isEmpty()) {
                lineas.add(BOM + unir(objeto.getEncabezados()));
            }

            String clave = escapar(valores[0]);
            String registro = unir(valores);
            boolean actualizado = false;

            for (int i = 1; i < lineas.size(); i++) { 
                String linea = lineas.get(i);
                if (linea.equals(clave) || linea.startsWith(clave + ",")) {
                    lineas.set(i, registro);
                    actualizado = true;
                    break;
                }
            }
            if (!actualizado) {
                lineas.add(registro);
            }

            Files.write(archivo, lineas, StandardCharsets.UTF_8);
            registrar("GUARDADO " + nombre + " [" + valores[0] + "]"
                    + (actualizado ? " (actualizado)" : " (nuevo)"));
            return true;

        } catch (IOException e) {
            System.out.println("Error al guardar en '" + CARPETA + "': " + e.getMessage());
            return false;
        }
    }

    
    public static synchronized void registrar(String accion) {
        Path archivo = ruta("historial.log");
        String linea = "[" + LocalDateTime.now().format(FORMATO_FECHA) + "] "
                + accion + System.lineSeparator();
        try {
            Files.createDirectories(archivo.getParent());
            Files.write(archivo, linea.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error al escribir el historial: " + e.getMessage());
        }
    }

    public static void error(String mensaje) {
        System.out.println("Error: " + mensaje);
        registrar("ERROR " + mensaje);
    }

    

    private static Path ruta(String archivo) {
        return Paths.get(CARPETA, archivo);
    }

    private static String unir(String[] campos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escapar(campos[i]));
        }
        return sb.toString();
    }

    
    private static String escapar(String valor) {
        if (valor == null) return "";
        String limpio = valor.replace("\r", " ").replace("\n", " ");
        if (limpio.contains(",") || limpio.contains("\"")) {
            return "\"" + limpio.replace("\"", "\"\"") + "\"";
        }
        return limpio;
    }
}