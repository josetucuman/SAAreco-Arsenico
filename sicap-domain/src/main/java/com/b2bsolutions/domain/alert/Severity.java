package com.b2bsolutions.domain.alert;

public enum Severity {

    INFO("Informativo", "#3B82F6", false),
    WARNING("Advertencia", "#F59E0B", false),
    CRITICAL("Crítico", "#EF4444", true);

    private final String descripcion;
    private final String colorHex;      // el front usa esto directamente
    private final boolean requiereAccionInmediata;

    Severity(String descripcion, String colorHex, boolean requiereAccionInmediata) {
        this.descripcion              = descripcion;
        this.colorHex                 = colorHex;
        this.requiereAccionInmediata  = requiereAccionInmediata;
    }

    public String getDescripcion()              { return descripcion; }
    public String getColorHex()                 { return colorHex; }
    public boolean isRequiereAccionInmediata()  { return requiereAccionInmediata; }
}