package com.b2bsolutions.domain.monitoring;

import com.b2bsolutions.domain.exception.DomainViolationException;

import java.util.Objects;

/**
 * Value Object que representa el rango aceptable para una métrica de calidad del agua.
 * Inmutable por ser record — garantiza que min siempre es menor que max.
 */
public record AcceptableRange(
        WaterMetric metrica,
        double min,
        double max
) {

    // ── Constructor compacto con validaciones ────────────────────────────────

    public AcceptableRange {
        Objects.requireNonNull(metrica, "La métrica es obligatoria");

        if (min < 0) {
            throw new DomainViolationException(
                    "El valor mínimo no puede ser negativo para " + metrica.getNombre()
            );
        }
        if (max <= min) {
            throw new DomainViolationException(
                    "El valor máximo (" + max + ") debe ser mayor al mínimo (" + min + ")"
                            + " para " + metrica.getNombre()
            );
        }
    }

    // ── Factory methods por métrica ──────────────────────────────────────────

    public static AcceptableRange paraArsenico() {
        return new AcceptableRange(WaterMetric.ARSENIC_UG_L, 0.0, 10.0);
    }

    public static AcceptableRange paraPh() {
        return new AcceptableRange(WaterMetric.PH, 6.5, 8.5);
    }

    public static AcceptableRange paraTurbidez() {
        return new AcceptableRange(WaterMetric.TURBIDITY_NTU, 0.0, 4.0);
    }

    public static AcceptableRange paraTemperatura() {
        return new AcceptableRange(WaterMetric.TEMPERATURE_C, 0.0, 25.0);
    }

    public static AcceptableRange paraConductividad() {
        return new AcceptableRange(WaterMetric.CONDUCTIVITY_US_CM, 400.0, 1000.0);
    }

    // ── Lógica de negocio ────────────────────────────────────────────────────

    /** ¿El valor está dentro del rango aceptable? */
    public boolean isWithin(double value) {
        return value >= min && value <= max;
    }

    /** ¿El valor supera el máximo permitido? */
    public boolean superaMaximo(double value) {
        return value > max;
    }

    /** ¿El valor está por debajo del mínimo? */
    public boolean bajaNinimo(double value) {
        return value < min;
    }

    /**
     * Porcentaje de desviación respecto al límite más cercano.
     * Retorna 0.0 si está dentro del rango.
     * Útil para el dashboard: cuánto le falta o cuánto se pasó.
     */
    public double porcentajeDesviacion(double value) {
        if (isWithin(value)) return 0.0;
        if (value > max) return ((value - max) / max) * 100.0;
        return ((min - value) / min) * 100.0;
    }

    /** Descripción del rango para el front */
    public String describir() {
        return metrica.getNombre() + ": ["
                + min + " – " + max
                + (metrica.getUnidad().isBlank() ? "" : " " + metrica.getUnidad())
                + "]";
    }
}