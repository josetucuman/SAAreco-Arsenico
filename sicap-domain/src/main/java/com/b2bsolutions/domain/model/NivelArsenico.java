package com.b2bsolutions.domain.model;

import com.b2bsolutions.domain.font.enums.Clasificacion;
import com.b2bsolutions.domain.shared.ConcentracionPPB;

import java.util.Objects;

/**
 * Value Object que representa el nivel de arsénico en una muestra de agua.
 * Encapsula la concentración medida y la clasifica según estándares OMS/EPA/SENASA.
 *
 * Límites de referencia (µg/L):
 *   ≤ 10   → ACEPTABLE   (OMS / EPA / SENASA Argentina)
 *   ≤ 50   → ELEVADO     (requiere seguimiento)
 *   > 50   → CRITICO     (riesgo para la salud, acción inmediata)
 */
public final class NivelArsenico {

    // Límites según OMS y normativa argentina (SENASA / CAA Artículo 982)
    public static final ConcentracionPPB LIMITE_OMS     = ConcentracionPPB.deMicrogramosPorLitro(10.0);
    public static final ConcentracionPPB LIMITE_ELEVADO = ConcentracionPPB.deMicrogramosPorLitro(50.0);

    private final ConcentracionPPB concentracion;
    private final Clasificacion clasificacion;

    // ── Constructor privado ───────────────────────────────────────────────────

    private NivelArsenico(ConcentracionPPB concentracion) {
        Objects.requireNonNull(concentracion, "La concentración no puede ser nula");
        this.concentracion = concentracion;
        this.clasificacion = clasificar(concentracion);
    }

    // ── Factory methods ───────────────────────────────────────────────────────

    public static NivelArsenico de(ConcentracionPPB concentracion) {
        return new NivelArsenico(concentracion);
    }

    public static NivelArsenico deMicrogramosPorLitro(double ugL) {
        return new NivelArsenico(ConcentracionPPB.deMicrogramosPorLitro(ugL));
    }

    /** Compatibilidad con AnalisisQuimico que puede recibir mg/L */
    public static NivelArsenico deMiligramosPorLitro(double mgL) {
        return new NivelArsenico(ConcentracionPPB.deMiligramosPorLitro(mgL));
    }

    // ── Lógica de negocio ─────────────────────────────────────────────────────

    public boolean esAceptable() {
        return clasificacion == Clasificacion.ACEPTABLE;
    }

    public boolean requiereAtencion() {
        return clasificacion == Clasificacion.ELEVADO
                || clasificacion == Clasificacion.CRITICO;
    }

    public boolean esCritico() {
        return clasificacion == Clasificacion.CRITICO;
    }

    public boolean superaLimiteOMS() {
        return concentracion.superaLimite(LIMITE_OMS);
    }

    /**
     * Calcula el porcentaje de reducción necesario para alcanzar el límite OMS.
     * Retorna 0.0 si ya está dentro del límite.
     * Útil para dimensionar el proceso de filtrado.
     */
    public double porcentajeReduccionNecesario() {
        if (!superaLimiteOMS()) return 0.0;
        double actual   = concentracion.enMicrogramosPorLitro();
        double objetivo = LIMITE_OMS.enMicrogramosPorLitro();
        return ((actual - objetivo) / actual) * 100.0;
    }

    /**
     * Verifica si luego del filtrado el resultado es aceptable.
     */
    public boolean filtradoEsEfectivo(NivelArsenico nivelPostFiltrado) {
        Objects.requireNonNull(nivelPostFiltrado, "El nivel post-filtrado no puede ser nulo");
        return nivelPostFiltrado.esAceptable();
    }

    // ── Clasificación interna ─────────────────────────────────────────────────

    private static Clasificacion clasificar(ConcentracionPPB c) {
        if (c.esMenorOIgualQue(LIMITE_OMS))     return Clasificacion.ACEPTABLE;
        if (c.esMenorOIgualQue(LIMITE_ELEVADO)) return Clasificacion.ELEVADO;
        return Clasificacion.CRITICO;
    }

    // ── Accesores ─────────────────────────────────────────────────────────────

    public ConcentracionPPB getConcentracion() { return concentracion; }
    public Clasificacion getClasificacion()     { return clasificacion; }

    // ── Value Object contract ─────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NivelArsenico that)) return false;
        return Objects.equals(this.concentracion, that.concentracion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concentracion);
    }

    @Override
    public String toString() {
        return "NivelArsenico{concentracion=" + concentracion
                + ", clasificacion=" + clasificacion + "}";
    }
}