package astore.productos;

import astore.util.Guardable;
import java.time.LocalDate;

/** Promoción con un porcentaje de descuento, una fecha límite y un perfil de cliente. */
public class Promocion implements Guardable {
    private String codigoPromocion;
    private String nombreCampana;
    private String descripcionRegla;
    private double porcentajeDescuento;
    private LocalDate vigenciaHasta;
    private String perfilExclusivo; // "Todos" o por ejemplo "Estudiantes"
    private boolean activa;

    public Promocion(String codigoPromocion, String nombreCampana, String descripcionRegla,
                     double porcentajeDescuento, LocalDate vigenciaHasta, String perfilExclusivo) {
        if (codigoPromocion == null || codigoPromocion.trim().isEmpty()) {
            throw new IllegalArgumentException("La promoción necesita un código.");
        }
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        if (vigenciaHasta == null) {
            throw new IllegalArgumentException("La promoción necesita una fecha de vigencia.");
        }
        this.codigoPromocion = codigoPromocion.trim().toUpperCase();
        this.nombreCampana = nombreCampana;
        this.descripcionRegla = descripcionRegla;
        this.porcentajeDescuento = porcentajeDescuento;
        this.vigenciaHasta = vigenciaHasta;
        this.perfilExclusivo = perfilExclusivo;
        this.activa = true;
    }

    public String getCodigoPromocion() { return codigoPromocion; }
    public String getNombreCampana() { return nombreCampana; }
    public void setNombreCampana(String nombreCampana) { this.nombreCampana = nombreCampana; }
    public String getDescripcionRegla() { return descripcionRegla; }
    public void setDescripcionRegla(String descripcionRegla) { this.descripcionRegla = descripcionRegla; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public LocalDate getVigenciaHasta() { return vigenciaHasta; }
    public String getPerfilExclusivo() { return perfilExclusivo; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    /** La promoción sirve si está activa y no ha pasado su fecha límite. */
    public boolean estaVigente(LocalDate hoy) {
        return activa && !hoy.isAfter(vigenciaHasta);
    }

    /** Revisa si la promoción es para todos o para el perfil indicado. */
    public boolean aplicaPara(String perfil) {
        return perfilExclusivo.equalsIgnoreCase("Todos") || perfilExclusivo.equalsIgnoreCase(perfil);
    }

    /** Cuánto se descuenta de un monto (redondeado a centavos). */
    public double calcularDescuento(double monto) {
        return Math.round(monto * porcentajeDescuento) / 100.0;
    }

    public void desactivarPromocion() {
        this.activa = false;
        guardar();
    }

    @Override
    public String getNombreArchivo() { return "promociones"; }

    @Override
    public String[] getEncabezados() {
        return new String[]{"codigo", "campana", "regla", "descuento", "vigencia_hasta", "perfil", "activa"};
    }

    @Override
    public String[] getValores() {
        return new String[]{codigoPromocion, nombreCampana, descripcionRegla,
                String.valueOf(porcentajeDescuento), vigenciaHasta.toString(),
                perfilExclusivo, String.valueOf(activa)};
    }
}
