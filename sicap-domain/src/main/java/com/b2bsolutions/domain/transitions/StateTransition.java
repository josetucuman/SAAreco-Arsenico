package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.state.PlantState;

import java.time.Instant;
import java.util.Objects;

/**
 * Registro inmutable de una transición de estado de la planta.
 * Sirve como historial auditable del ciclo de vida de PlantState.
 */
public record StateTransition(
        PlantState from,
        PlantState to,
        String reason,          // nullable — puede no haber razón registrada
        String triggeredBy,     // nullable — puede ser automático sin operador
        Instant occurredAt
) {

    // ── Constructor compacto con validaciones ────────────────────────────────

    public StateTransition {
        Objects.requireNonNull(from,        "from es obligatorio");
        Objects.requireNonNull(to,          "to es obligatorio");
        Objects.requireNonNull(occurredAt,  "occurredAt es obligatorio");

        if (from == to) {
            throw new ReglaNegocioException(
                    "from y to no pueden ser iguales: " + from
            );
        }
        if (reason != null && reason.isBlank()) {
            throw new ReglaNegocioException("reason no puede ser blank si se provee");
        }
        if (triggeredBy != null && triggeredBy.isBlank()) {
            throw new ReglaNegocioException("triggeredBy no puede ser blank si se provee");
        }
    }

    // ── Factory methods ──────────────────────────────────────────────────────

    public static StateTransition of(PlantState from, PlantState to,
                                     String triggeredBy, String reason) {
        return new StateTransition(from, to, reason, triggeredBy, Instant.now());
    }

    public static StateTransition automatica(PlantState from, PlantState to, String reason) {
        return new StateTransition(from, to, reason, "SYSTEM", Instant.now());
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    public boolean fueAutomatica() {
        return "SYSTEM".equalsIgnoreCase(triggeredBy);
    }

    public boolean esTransicionDeEmergencia() {
        return to == PlantState.EMERGENCY || to == PlantState.SHUTTING_DOWN;
    }
}