package com.b2bsolutions.domain.font.enums;

public enum EstadoSanitario {

    DESCONOCIDO("Sin evaluación", false),
    EN_OBSERVACION("Bajo seguimiento", false),
    APTA("Apta para consumo humano", true),
    CONTAMINADA("Contaminada — no apta", false),
    EN_CUARENTENA("Aislada — pendiente de tratamiento", false);

    private final String descripcion;
    private final boolean aptaParaConsumo;

    EstadoSanitario(String descripcion, boolean aptaParaConsumo) {
        this.descripcion = descripcion;
        this.aptaParaConsumo = aptaParaConsumo;
    }

    public String getDescripcion() { return descripcion; }

    /** El front puede usar esto directamente para pintar verde/rojo */
    public boolean isAptaParaConsumo() { return aptaParaConsumo; }

    /** ¿Requiere acción inmediata? */
    public boolean requiereAccion() {
        return this == CONTAMINADA || this == EN_CUARENTENA;
    }
}