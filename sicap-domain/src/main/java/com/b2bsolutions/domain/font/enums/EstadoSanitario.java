package com.b2bsolutions.domain.font.enums;

public enum EstadoSanitario {

    DESCONOCIDO("Sin evaluación",                       false, "#6B7280"),
    EN_OBSERVACION("Bajo seguimiento",                  false, "#F59E0B"),
    APTA("Apta para consumo humano",                    true,  "#10B981"),
    CONTAMINADA("Contaminada — no apta",                false, "#EF4444"),
    EN_CUARENTENA("Aislada — pendiente de tratamiento", false, "#8B5CF6");

    private final String descripcion;
    private final boolean aptaParaConsumo;
    private final String colorHex;

    EstadoSanitario(String descripcion, boolean aptaParaConsumo, String colorHex) {
        this.descripcion     = descripcion;
        this.aptaParaConsumo = aptaParaConsumo;
        this.colorHex        = colorHex;
    }

    public String getDescripcion()    { return descripcion; }
    public boolean isAptaParaConsumo(){ return aptaParaConsumo; }
    public String getColorHex()       { return colorHex; }

    public boolean requiereAccion() {
        return this == CONTAMINADA || this == EN_CUARENTENA;
    }
}