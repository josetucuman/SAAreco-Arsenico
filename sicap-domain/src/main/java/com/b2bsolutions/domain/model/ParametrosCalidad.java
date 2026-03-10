package com.b2bsolutions.domain.model;


import com.b2bsolutions.domain.exception.DomainViolationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object que agrupa los parámetros fisicoquímicos de calidad del agua.
 * Reemplaza los doubles crudos de tu AnalisisQuimico con semántica y validación.
 *
 * Rangos según CAA (Código Alimentario Argentino) y OMS:
 *   pH           : 6.5 – 8.5
 *   Turbidez     : 0 – 4 NTU (apta consumo), hasta 5 NTU tolerable
 *   Temperatura  : 0 – 35 °C
 */
public class ParametrosCalidad {

    // ── Rangos permitidos ────────────────────────────────────────────────────
    private static final double PH_MIN           = 0.0;
    private static final double PH_MAX           = 14.0;
    private static final double PH_OPTIMO_MIN    = 6.5;
    private static final double PH_OPTIMO_MAX    = 8.5;

    private static final double TURBIDEZ_MIN     = 0.0;
    private static final double TURBIDEZ_MAX     = 1_000.0; // NTU — límite físico
    private static final double TURBIDEZ_APTA    = 4.0;     // NTU — OMS consumo humano

    private static final double TEMP_MIN         = 0.0;
    private static final double TEMP_MAX         = 100.0;   // °C
    private static final double TEMP_OPTIMA_MAX  = 25.0;    // °C — referencia OMS

    // ── Campos ───────────────────────────────────────────────────────────────
    private final double ph;
    private final double turbidezNTU;
    private final double temperaturaC;


    public ParametrosCalidad(double ph, double turbidezNTU, double temperaturaC) {
        validarPh(ph);
        validarTurbidez(turbidezNTU);
        validarTemperatura(temperaturaC);

        this.ph = ph;
        this.turbidezNTU = turbidezNTU;
        this.temperaturaC = temperaturaC;
    }
// ── Factory method ───────────────────────────────────────────────────────

    public static ParametrosCalidad de(double ph, double turbidezNTU, double temperaturaC) {
        return new ParametrosCalidad(ph, turbidezNTU, temperaturaC);
    }

    // ── Evaluaciones de negocio ──────────────────────────────────────────────

    public boolean phEsOptimo() {
        return ph >= PH_OPTIMO_MIN && ph <= PH_OPTIMO_MAX;
    }

    public boolean turbidezEsAptaParaConsumo() {
        return turbidezNTU <= TURBIDEZ_APTA;
    }

    public boolean temperaturaEsOptima() {
        return temperaturaC <= TEMP_OPTIMA_MAX;
    }

    /**
     * Evaluación integral: todos los parámetros dentro de rangos óptimos.
     * No incluye arsénico — ese es dominio de NivelArsenico.
     */
    public boolean todosLosParametrosSonAceptables() {
        return phEsOptimo() && turbidezEsAptaParaConsumo();
    }

    /**
     * Retorna lista de parámetros fuera de rango óptimo.
     * Útil para mostrar en el dashboard del front.
     */

    public List<String> parametrosFueraDeRango() {
        List<String> fuera = new ArrayList<>();
        if (!phEsOptimo()) {
            fuera.add("pH: " + ph + " (óptimo: " + PH_OPTIMO_MIN + "–" + PH_OPTIMO_MAX + ")");
        }
        if (!turbidezEsAptaParaConsumo()) {
            fuera.add("Turbidez: " + turbidezNTU + " NTU (máximo apto: " + TURBIDEZ_APTA + " NTU)");
        }
        if (!temperaturaEsOptima()) {
            fuera.add("Temperatura: " + temperaturaC + " °C (óptimo: ≤" + TEMP_OPTIMA_MAX + " °C)");
        }
        return Collections.unmodifiableList(fuera);
    }

    // ── Accesores ────────────────────────────────────────────────────────────

    public double getPh() { return ph; }

    public double getTurbidezNTU() { return turbidezNTU; }

    public double getTemperaturaC() { return temperaturaC; }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private static void validarPh(double ph) {
        if (ph < PH_MIN || ph > PH_MAX) {
            throw new DomainViolationException(
                    "pH fuera de rango físico posible [0–14]. Valor recibido: " + ph
            );
        }
    }

    private static void validarTurbidez(double turbidez) {
        if (turbidez < TURBIDEZ_MIN || turbidez > TURBIDEZ_MAX) {
            throw new DomainViolationException(
                    "Turbidez fuera de rango [0–" + TURBIDEZ_MAX + " NTU]. Valor recibido: " + turbidez
            );
        }
    }

    private static void validarTemperatura(double temp) {
        if (temp < TEMP_MIN || temp > TEMP_MAX) {
            throw new DomainViolationException(
                    "Temperatura fuera de rango [0–" + TEMP_MAX + " °C]. Valor recibido: " + temp
            );
        }
    }

    // ── Value Object contract ────────────────────────────────────────────────


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParametrosCalidad that = (ParametrosCalidad) o;
        return Double.compare(ph, that.ph) == 0 && Double.compare(turbidezNTU, that.turbidezNTU) == 0 && Double.compare(temperaturaC, that.temperaturaC) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ph, turbidezNTU, temperaturaC);
    }

    @Override
    public String toString() {
        return "ParametrosCalidad{" +
                "ph=" + ph +
                ", turbidezNTU=" + turbidezNTU +
                ", temperaturaC=" + temperaturaC +
                '}';
    }
}
