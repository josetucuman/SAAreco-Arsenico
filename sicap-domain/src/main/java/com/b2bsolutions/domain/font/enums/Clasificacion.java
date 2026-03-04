package com.b2bsolutions.domain.font.enums;

public enum Clasificacion {
    ACEPTABLE("Dentro de límites OMS/SENASA. Agua apta para consumo."),
    ELEVADO("Supera límite OMS. Requiere seguimiento y posible tratamiento."),
    CRITICO("Supera 50 µg/L. Riesgo para la salud. Acción inmediata requerida.");

    private final String descripcion;

    Clasificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
