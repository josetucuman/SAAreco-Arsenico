package com.b2bsolutions.domain.font.enums;

public enum TipoFuente {

    POZO("Perforación subterránea"),
    TANQUE("Almacenamiento elevado o enterrado"),
    RED("Red de distribución pública"),
    PLANTA("Planta de tratamiento");

    private final String descripcion;

    TipoFuente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }
}