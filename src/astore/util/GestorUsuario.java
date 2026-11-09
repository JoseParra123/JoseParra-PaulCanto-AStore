package astore.util;

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

/**
 * Se encarga de guardar y leer los datos en archivos CSV (la tienda no usa base de datos).
 * Los archivos se guardan en la carpeta "datos", junto a un historial.log con lo que pasó.
 * Los datos en el archivo sobreviven aunque se cierre el programa.
 */
public final class GestorUsuario {

    public static final String CARPETA = "datos";

    // El BOM hace que Excel abra bien los acentos de los archivos CSV
    private static final String BOM = "﻿";
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private GestorUsuario() {}

    /** Guarda un objeto: si ya existe una fila con su clave la actualiza, si no, agrega una nueva. */
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

    /** Lee un archivo CSV y devuelve sus filas (sin el encabezado). Si no existe, devuelve una lista vacía. */
    public static synchronized List<String[]> cargar(String nombreArchivo) {
        List<String[]> filas = new ArrayList<>();
        Path archivo = ruta(nombreArchivo + ".csv");
        if (!Files.exists(archivo)) {
            return filas;
        }
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) { // la línea 0 es el encabezado
                if (!lineas.get(i).trim().isEmpty()) {
                    filas.add(separar(lineas.get(i)));
                }
            }
        } catch (IOException e) {
            error("No se pudo leer " + nombreArchivo + ".csv: " + e.getMessage());
        }
        return filas;
    }

    /** Escribe una línea en el historial.log */
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

    /** Muestra un error en pantalla y lo anota en el historial. */
    public static void error(String mensaje) {
        System.out.println("Error: " + mensaje);
        registrar("ERROR " + mensaje);
    }

    // ---------- Métodos de apoyo ----------

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

    /** Si el valor tiene comas o comillas lo encierra entre comillas, como lo pide el formato CSV. */
    private static String escapar(String valor) {
        if (valor == null) return "";
        String limpio = valor.replace("\r", " ").replace("\n", " ");
        if (limpio.contains(",") || limpio.contains("\"")) {
            return "\"" + limpio.replace("\"", "\"\"") + "\"";
        }
        return limpio;
    }

    /** Divide una línea CSV en campos, respetando las comillas. */
    private static String[] separar(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (entreComillas) {
                if (c == '"' && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    actual.append('"'); // comilla doble escapada
                    i++;
                } else if (c == '"') {
                    entreComillas = false;
                } else {
                    actual.append(c);
                }
            } else if (c == '"') {
                entreComillas = true;
            } else if (c == ',') {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        campos.add(actual.toString());
        return campos.toArray(new String[0]);
    }
}
