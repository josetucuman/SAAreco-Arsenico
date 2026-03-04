package com.b2bsolutions.domain.monitoring;

/**
 * Métricas fisicoquímicas monitoreadas en el sistema de purificación.
 * Cada métrica conoce su unidad, descripción y si es crítica para consumo humano.
 */
public enum WaterMetric {

    ARSENIC_UG_L(
            "Arsénico",
            "µg/L",
            true,
            "Contaminante principal — límite OMS 10 µg/L"
    ),
    PH(
            "pH",
            "",
            true,
            "Acidez/alcalinidad — rango óptimo 6.5–8.5"
    ),
    TURBIDITY_NTU(          // ← corregido: TUBIDITY → TURBIDITY
            "Turbidez",
            "NTU",
            true,
            "Partículas en suspensión — máximo 4 NTU para consumo"
    ),
    CONDUCTIVITY_US_CM(
            "Conductividad",
            "µS/cm",
            false,
            "Minerales disueltos — referencia 400–1000 µS/cm"
    ),
    TEMPERATURE_C(
            "Temperatura",
            "°C",
            false,
            "Temperatura del agua — referencia ≤25 °C"
    ),
    FLOW_M3_H(
            "Caudal",
            "m³/h",
            false,
            "Flujo volumétrico del sistema"
    );

    private final String nombre;
    private final String unidad;
    private final boolean criticaParaConsumo;
    private final String descripcion;

    WaterMetric(String nombre, String unidad, boolean criticaParaConsumo, String descripcion) {
        this.nombre             = nombre;
        this.unidad             = unidad;
        this.criticaParaConsumo = criticaParaConsumo;
        this.descripcion        = descripcion;
    }

    public String getNombre()                { return nombre; }
    public String getUnidad()                { return unidad; }
    public boolean isCriticaParaConsumo()    { return criticaParaConsumo; }
    public String getDescripcion()           { return descripcion; }

    /** Formato display para el dashboard: "Arsénico (µg/L)" */
    public String getLabel() {
        return unidad.isBlank() ? nombre : nombre + " (" + unidad + ")";
    }
}