package com.b2bsolutions.domain.shared;

import com.b2bsolutions.domain.exception.DomainViolationException;

import java.util.Objects;

/**
 * Value Object que representa una concentración en microgramos por litro (µg/L = PPB).
 * Unidad estándar OMS para medición de arsénico en agua potable.
 * Límite OMS: 10 µg/L | Límite EPA: 10 µg/L | Límite SENASA Argentina: 10 µg/L
 */
public class ConcentracionPPB {
    private static final double VALOR_MINIMO = 0.0;
    private static final double VALOR_MAXIMO = 5_000.0; // µg/L — límite físico razonable
    private static final double FACTOR_MGL_A_PPB = 1_000.0;

    private final double valor; // en µg/L

    private ConcentracionPPB(double valor) {
        validar(valor);
        this.valor = valor;
    }
    // ── Factory methods ──────────────────────────────────────────────────────

    public static ConcentracionPPB deMicrogramosPorLitro(double ugL) {
        return new ConcentracionPPB(ugL);
    }

    public static ConcentracionPPB deMiligramosPorLitro(double mgL) {
        // Conversión: 1 mg/L = 1000 µg/L
        return new ConcentracionPPB(mgL * FACTOR_MGL_A_PPB);
    }

    public static ConcentracionPPB cero() {
        return new ConcentracionPPB(0.0);
    }

    // ── Accesores ────────────────────────────────────────────────────────────

    public double enMicrogramosPorLitro() {
        return valor;
    }

    public double enMiligramosPorLitro() {
        return valor / FACTOR_MGL_A_PPB;
    }
// ── Comparaciones de negocio ─────────────────────────────────────────────

    public boolean superaLimite(ConcentracionPPB limite) {
        Objects.requireNonNull(limite, "El límite no puede ser nulo");
        return this.valor > limite.valor;
    }

    public boolean esMenorOIgualQue(ConcentracionPPB otro) {
        Objects.requireNonNull(otro, "La concentración a comparar no puede ser nula");
        return this.valor <= otro.valor;
    }

    public ConcentracionPPB reducirEn(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new DomainViolationException("El porcentaje de reducción debe estar entre 0 y 100");
        }
        return new ConcentracionPPB(this.valor * (1 - porcentaje / 100.0));
    }

    // ── Validación ───────────────────────────────────────────────────────────


    private static void validar(double valor) {
        if (valor < VALOR_MINIMO) {
            throw new DomainViolationException(
                    "La concentración no puede ser negativa. Valor recibido: " + valor + " µg/L"
            );
        }
        if (valor > VALOR_MAXIMO) {
            throw new DomainViolationException(
                    "La concentración supera el límite físico razonable de " + VALOR_MAXIMO + " µg/L. Valor: " + valor
            );
        }
    }


    // ── Value Object contract ────────────────────────────────────────────────


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConcentracionPPB that = (ConcentracionPPB) o;
        return Double.compare(valor, that.valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}


