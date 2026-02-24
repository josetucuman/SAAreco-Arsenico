package com.b2bsolutions.domain.model;

import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.StateTransitionService;

import java.time.Instant;
import java.util.UUID;

public final class MuestraAgua {

    private final UUID id;
    private final String puntoOrigen;

    private final Instant fechaToma;

    private Double arsenicoMugL;
    private State estado;
    private Instant fechaEvaluacion;

    public MuestraAgua(UUID id, String puntoOrigen, Instant fechaToma) {
        this.id = id;
        this.puntoOrigen = puntoOrigen;
        this.fechaToma = fechaToma;
    }

    /* ========================
     Comportamiento de dominio
     ========================= */
    public void registrarAnalisis(double arsenicoMugL,
                                  StateTransitionService transitions){

        if(this.estado != State.TOMADA){
            throw new ReglaNegocioException(
                    "La muestra no puede analizarse desde el estado: " + estado
            );
        }

        if (arsenicoMugL < 0) {
            throw new ReglaNegocioException(
                    "El valor de arsénico no puede ser negativo"
            );
        }

        transitions.validate(State.TOMADA, State.EN_ANALISIS);

        this.arsenicoMugL = arsenicoMugL;
        this.estado = State.EN_ANALISIS;
        this.fechaEvaluacion = Instant.now();
    }

    public void evaluar(double limiteMaximo,
                        StateTransitionService transitions) {

        if (estado != State.EN_ANALISIS) {
            throw new ReglaNegocioException(
                    "La muestra debe estar en análisis para evaluarse"
            );
        }

        State destino = arsenicoMugL <= limiteMaximo
                ? State.APTA
                : State.NO_APTA;

        transitions.validate(State.EN_ANALISIS, destino);

        this.estado = destino;
    }

    /* =======================
       Getters (sin setters)
       ======================= */

    public UUID getId() {
        return id;
    }

    public String getPuntoOrigen() {
        return puntoOrigen;
    }

    public Instant getFechaToma() {
        return fechaToma;
    }

    public Double getArsenicoMugL() {
        return arsenicoMugL;
    }

    public State getEstado() {
        return estado;
    }

    public Instant getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    /* =======================
       Validaciones internas
       ======================= */

    private String validarOrigen(String origen) {
        if (origen == null || origen.isBlank()) {
            throw new ReglaNegocioException(
                    "El punto de origen es obligatorio"
            );
        }
        return origen;
    }
}
