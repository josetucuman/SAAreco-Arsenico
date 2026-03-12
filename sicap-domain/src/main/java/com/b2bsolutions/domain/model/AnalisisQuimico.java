package com.b2bsolutions.domain.model;

import java.time.Instant;
import java.util.Objects;


public final class AnalisisQuimico {
    private final NivelArsenico nivelArsenico;       // reemplaza arsenicoMgL
    private final ParametrosCalidad parametros;       // reemplaza ph, turbidez, temp
    private final Instant fechaAnalisis;

    public AnalisisQuimico(NivelArsenico nivelArsenico, ParametrosCalidad parametros, Instant fechaAnalisis) {
        Objects.requireNonNull(nivelArsenico, "El nivel de arsénico es obligatorio");
        Objects.requireNonNull(parametros, "Los parámetros de calidad son obligatorios");
        Objects.requireNonNull(fechaAnalisis, "La fecha de análisis es obligatoria");

        this.nivelArsenico = nivelArsenico;
        this.parametros = parametros;
        this.fechaAnalisis = fechaAnalisis;
    }

    /** Evaluación integral del análisis — útil para disparar alertas */
    public boolean requiereIntervencion() {
        return nivelArsenico.requiereAtencion()
                || !parametros.todosLosParametrosSonAceptables();
    }

    public boolean esCritico() {
        return nivelArsenico.esCritico();
    }

    public NivelArsenico getNivelArsenico() { return nivelArsenico; }
    public ParametrosCalidad getParametros() { return parametros; }
    public Instant getFechaAnalisis() { return fechaAnalisis; }
}




