package com.b2bsolutions.domain.font;

import java.util.Objects;

public final class Ubicacion {

    private final String localidad;
    private final String provincia;
    private final double latitud;
    private final double longitud;

    public Ubicacion(String localidad, String provincia, double latitud, double longitud) {
        this.localidad = localidad;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ubicacion ubicacion = (Ubicacion) o;
        return Double.compare(latitud, ubicacion.latitud) == 0 && Double.compare(longitud, ubicacion.longitud) == 0 && Objects.equals(localidad, ubicacion.localidad) && Objects.equals(provincia, ubicacion.provincia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localidad, provincia, latitud, longitud);
    }
}
