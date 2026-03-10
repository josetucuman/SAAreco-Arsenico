package com.b2bsolutions.domain.font;

import com.b2bsolutions.domain.exception.ReglaNegocioException;

import java.util.Objects;

public final class Ubicacion {

    private final String localidad;
    private final String provincia;
    private final double latitud;
    private final double longitud;

    private static final double LAT_MIN = -90.0;
    private static final double LAT_MAX =  90.0;
    private static final double LON_MIN = -180.0;
    private static final double LON_MAX =  180.0;


    public Ubicacion(String localidad, String provincia, double latitud, double longitud) {
        this.localidad = validarTexto(localidad, "localidad");
        this.provincia = validarTexto(provincia, "provincia");
        this.latitud   = validarLatitud(latitud);
        this.longitud  = validarLongitud(longitud);
    }

    // ── Factory method ───────────────────────────────────────────────────────

    public static Ubicacion de(String localidad, String provincia,
                               double latitud, double longitud) {
        return new Ubicacion(localidad, provincia, latitud, longitud);
    }


    // ── Validaciones ─────────────────────────────────────────────────────────

    private static String validarTexto(String valor, String campo){
        if(valor == null || valor.isBlank()){
            throw new ReglaNegocioException("El campo " + campo + " es obligatorio");
        }
        return valor.trim();
    }


    private static double validarLatitud(double lat){
        if (lat < LAT_MIN || lat > LAT_MAX) {
            throw new ReglaNegocioException(
                    "Latitud inválida: " + lat + ". Rango válido [" + LAT_MIN + ", " + LAT_MAX + "]"
            );
        }
        return lat;
    }

    private static double validarLongitud(double lon) {
        if (lon < LON_MIN || lon > LON_MAX) {
            throw new ReglaNegocioException(
                    "Longitud inválida: " + lon + ". Rango válido [" + LON_MIN + ", " + LON_MAX + "]"
            );
        }
        return lon;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

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

    @Override
    public String toString() {
        return "Ubicacion{" +
                "localidad='" + localidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
