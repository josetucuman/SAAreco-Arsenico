package com.b2bsolutions.domain.font;

import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.font.enums.EstadoSanitario;
import com.b2bsolutions.domain.font.enums.TipoFuente;
import com.b2bsolutions.domain.transitions.events.DomainEvent;
import com.b2bsolutions.domain.transitions.events.EstadoSanitarioFuenteActualizadoEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root que representa una fuente de agua bajo monitoreo.
 *
 * Invariantes:
 *   - Toda fuente inicia en DESCONOCIDO hasta su primera evaluación
 *   - No se puede transicionar al mismo estado sanitario actual
 *   - nombre, tipo y ubicacion son obligatorios e inmutables
 */
public final class FuenteAgua {

    private final UUID id;
    private final String nombre;
    private final TipoFuente tipo;
    private final Ubicacion ubicacion;
    private EstadoSanitario estadoSanitario;

    private final List<DomainEvent> eventos = new ArrayList<>();

    // ── Constructor ──────────────────────────────────────────────────────────

    public FuenteAgua(UUID id, String nombre, TipoFuente tipo, Ubicacion ubicacion) {
        this.id              = Objects.requireNonNull(id,        "El ID es obligatorio");
        this.nombre          = validarNombre(nombre);
        this.tipo            = Objects.requireNonNull(tipo,      "El tipo de fuente es obligatorio");
        this.ubicacion       = Objects.requireNonNull(ubicacion, "La ubicación es obligatoria");
        this.estadoSanitario = EstadoSanitario.DESCONOCIDO;
    }

    // ── Factory method ───────────────────────────────────────────────────────

    public static FuenteAgua registrar(String nombre, TipoFuente tipo, Ubicacion ubicacion) {
        return new FuenteAgua(UUID.randomUUID(), nombre, tipo, ubicacion);
    }

    // ── Comportamiento de dominio ────────────────────────────────────────────

    public void actualizarEstadoSanitario(EstadoSanitario nuevoEstado) {
        Objects.requireNonNull(nuevoEstado, "El nuevo estado sanitario es obligatorio");

        if (this.estadoSanitario == nuevoEstado) {
            throw new ReglaNegocioException(
                    "La fuente ya se encuentra en estado: " + nuevoEstado
            );
        }

        EstadoSanitario anterior = this.estadoSanitario;
        this.estadoSanitario = nuevoEstado;

        eventos.add(new EstadoSanitarioFuenteActualizadoEvent(
                this.id,
                anterior,
                nuevoEstado,
                null,
                null,
                "SYSTEM",
                Instant.now()
        ));
    }

    /** Fuente contaminada por resultado de muestra NO_APTA */
    public void contaminar(UUID muestraId) {
        EstadoSanitario anterior = this.estadoSanitario;
        this.estadoSanitario = EstadoSanitario.CONTAMINADA;

        eventos.add(EstadoSanitarioFuenteActualizadoEvent.porMuestraNoApta(
                this.id, anterior, muestraId
        ));
    }

    /** Fuente rehabilitada luego de proceso de filtrado exitoso */
    public void rehabilitar(UUID muestraId) {
        EstadoSanitario anterior = this.estadoSanitario;
        this.estadoSanitario = EstadoSanitario.APTA;

        eventos.add(EstadoSanitarioFuenteActualizadoEvent.porFiltradoExitoso(
                this.id, anterior, muestraId
        ));
    }

    // ── Queries de negocio ───────────────────────────────────────────────────

    public boolean estaApta() {
        return estadoSanitario == EstadoSanitario.APTA;
    }

    public boolean estaContaminada() {
        return estadoSanitario == EstadoSanitario.CONTAMINADA;
    }

    public boolean requiereEvaluacion() {
        return estadoSanitario == EstadoSanitario.DESCONOCIDO
                || estadoSanitario == EstadoSanitario.EN_CUARENTENA;
    }

    // ── Eventos ──────────────────────────────────────────────────────────────

    public List<DomainEvent> pullEventos() {
        List<DomainEvent> copia = Collections.unmodifiableList(new ArrayList<>(eventos));
        eventos.clear();
        return copia;
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaNegocioException("El nombre de la fuente es obligatorio");
        }
        return nombre.trim();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public UUID getId()                         { return id; }
    public String getNombre()                   { return nombre; }
    public TipoFuente getTipo()                 { return tipo; }
    public Ubicacion getUbicacion()             { return ubicacion; }
    public EstadoSanitario getEstadoSanitario() { return estadoSanitario; }
}