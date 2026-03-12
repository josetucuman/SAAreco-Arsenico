package com.b2bsolutions.domain.transitions.events;

import com.b2bsolutions.domain.state.State;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Evento de dominio que se publica cada vez que un Aggregate Root
 * realiza una transición de estado exitosa.
 *
 * Es genérico y transversal — sirve para cualquier entidad
 * que use la máquina de estados (MuestraAgua, FuenteAgua, PlantState)
 */
public record StateTransitionOccurredEvent(
        UUID aggregateId,
        State fromState,
        State toState,
        String reason,
        String triggeredBy,
        Instant ocurridoEn
) implements DomainEvent {

    // ── Constructor compacto con validaciones ────────────────────────────────

    public StateTransitionOccurredEvent {
        Objects.requireNonNull(aggregateId, "aggregateId es obligatorio");
        Objects.requireNonNull(fromState,   "fromState es obligatorio");
        Objects.requireNonNull(toState,     "toState es obligatorio");
        Objects.requireNonNull(ocurridoEn,  "ocurridoEn es obligatorio");

        if (fromState == toState) {
            throw new IllegalArgumentException(
                    "fromState y toState no pueden ser iguales: " + fromState
            );
        }
        if (reason != null && reason.isBlank()) {
            throw new IllegalArgumentException(
                    "reason no puede ser blank si se provee"
            );
        }
        if (triggeredBy != null && triggeredBy.isBlank()) {
            throw new IllegalArgumentException(
                    "triggeredBy no puede ser blank si se provee"
            );
        }
    }

    // ── DomainEvent contract ─────────────────────────────────────────────────

    @Override
    public String eventType() {
        return "STATE_TRANSITION_OCCURRED";
    }

    // ── Queries de negocio ───────────────────────────────────────────────────

    public String describir() {
        return "[" + aggregateId + "] " + fromState + " → " + toState
                + (reason      != null ? " | Razón: " + reason      : "")
                + (triggeredBy != null ? " | Por: "   + triggeredBy : "");
    }

    public boolean fueAutomatico() {
        return "SYSTEM".equalsIgnoreCase(triggeredBy);
    }

    public boolean esTransicionDeEmergencia() {
        return toState == State.NO_APTA;
    }
}