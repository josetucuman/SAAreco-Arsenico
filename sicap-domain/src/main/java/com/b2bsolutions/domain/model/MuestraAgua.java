package com.b2bsolutions.domain.model;

import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.StateTransitionService;
import com.b2bsolutions.domain.transitions.events.DomainEvent;
import com.b2bsolutions.domain.transitions.events.MuestraEnAnalisisEvent;
import com.b2bsolutions.domain.transitions.events.MuestraEvaluadaEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class MuestraAgua {

    private final UUID id;
    private final UUID fuenteAguaId;
    private final String puntoOrigen;
    private final Instant fechaToma;

    private State estado;
    private AnalisisQuimico analisisQuimico;
    private Instant fechaEvaluacion;

    private final List<DomainEvent> eventos = new ArrayList<>();

    // ── Constructor ──────────────────────────────────────────────────────────

    public MuestraAgua(UUID id, UUID fuenteAguaId, String puntoOrigen, Instant fechaToma) {
        this.id           = Objects.requireNonNull(id,           "El ID es obligatorio");
        this.fuenteAguaId = Objects.requireNonNull(fuenteAguaId, "La fuente de agua es obligatoria");
        this.puntoOrigen  = validarPuntoOrigen(puntoOrigen);
        this.fechaToma    = Objects.requireNonNull(fechaToma,    "La fecha de toma es obligatoria");
        this.estado       = State.TOMADA;
    }

    // ── Factory method ───────────────────────────────────────────────────────

    public static MuestraAgua tomar(UUID fuenteAguaId, String puntoOrigen) {
        return new MuestraAgua(UUID.randomUUID(), fuenteAguaId, puntoOrigen, Instant.now());
    }

    // ── Comportamiento de dominio ────────────────────────────────────────────

    public void registrarAnalisis(AnalisisQuimico analisis,
                                  StateTransitionService transitions) {
        Objects.requireNonNull(analisis,     "El análisis químico es obligatorio");
        Objects.requireNonNull(transitions,  "El servicio de transiciones es obligatorio");

        validarEstado(State.TOMADA, "registrar análisis");
        transitions.validate(State.TOMADA, State.EN_ANALISIS);

        this.analisisQuimico = analisis;
        this.estado          = State.EN_ANALISIS;

        eventos.add(new MuestraEnAnalisisEvent(this.id, this.fuenteAguaId, destino, Instant.now()));
    }

    public void evaluar(StateTransitionService transitions) {
        Objects.requireNonNull(transitions, "El servicio de transiciones es obligatorio");

        validarEstado(State.EN_ANALISIS, "evaluar");

        if (analisisQuimico == null) {
            throw new ReglaNegocioException(
                    "No se puede evaluar una muestra sin análisis químico registrado"
            );
        }

        State destino = determinarEstadoFinal();
        transitions.validate(State.EN_ANALISIS, destino);

        this.estado          = destino;
        this.fechaEvaluacion = Instant.now();

        eventos.add(new MuestraEvaluadaEvent(this.id, this.fuenteAguaId, destino, Instant.now()));
    }

    // ── Lógica de decisión ───────────────────────────────────────────────────

    private State determinarEstadoFinal() {
        NivelArsenico nivel = analisisQuimico.getNivelArsenico();

        if (nivel.esCritico()) return State.NO_APTA;

        if (nivel.superaLimiteOMS()
                || !analisisQuimico.getParametros().todosLosParametrosSonAceptables()) {
            return State.NO_APTA;
        }

        return State.APTA;
    }

    // ── Queries de negocio ───────────────────────────────────────────────────

    public boolean estaApta() {
        return estado == State.APTA;
    }

    public boolean requiereFiltrado() {
        return estado == State.NO_APTA
                || (analisisQuimico != null && analisisQuimico.getNivelArsenico().superaLimiteOMS());
    }

    public boolean fueEvaluada() {
        return estado == State.APTA || estado == State.NO_APTA;
    }

    // ── Eventos ──────────────────────────────────────────────────────────────

    public List<DomainEvent> pullEventos() {
        List<DomainEvent> copia = Collections.unmodifiableList(new ArrayList<>(eventos));
        eventos.clear();
        return copia;
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private void validarEstado(State estadoEsperado, String operacion) {
        if (this.estado != estadoEsperado) {
            throw new ReglaNegocioException(
                    "No se puede [" + operacion + "] desde el estado: " + this.estado
                            + ". Estado requerido: " + estadoEsperado
            );
        }
    }

    private static String validarPuntoOrigen(String origen) {
        if (origen == null || origen.isBlank()) {
            throw new ReglaNegocioException("El punto de origen es obligatorio");
        }
        return origen.trim();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public UUID getId()                         { return id; }
    public UUID getFuenteAguaId()               { return fuenteAguaId; }
    public String getPuntoOrigen()              { return puntoOrigen; }
    public Instant getFechaToma()               { return fechaToma; }
    public State getEstado()                    { return estado; }
    public AnalisisQuimico getAnalisisQuimico() { return analisisQuimico; }
    public Instant getFechaEvaluacion()         { return fechaEvaluacion; }
}