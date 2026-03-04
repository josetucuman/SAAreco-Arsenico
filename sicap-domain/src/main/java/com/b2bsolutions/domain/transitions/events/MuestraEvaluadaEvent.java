package com.b2bsolutions.domain.transitions.events;

import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.event.DomainEvent;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Evento de dominio que se publica cuando una MuestraAgua completa su evaluación.
 * Transporta el resultado final (APTA | NO_APTA) para que otros componentes
 * puedan reaccionar: generar alertas, iniciar proceso de filtrado, actualizar dashboard.
 */
public record MuestraEvaluadaEvent(
        UUID muestraId,
        UUID fuenteAguaId,
        State estadoFinal,
        Instant ocurridoEn
) implements DomainEvent {

    // ── Constructor compacto con validaciones ────────────────────────────────

    public MuestraEvaluadaEvent {
        Objects.requireNonNull(muestraId,    "muestraId es obligatorio");
        Objects.requireNonNull(fuenteAguaId, "fuenteAguaId es obligatorio");
        Objects.requireNonNull(estadoFinal,  "estadoFinal es obligatorio");
        Objects.requireNonNull(ocurridoEn,   "ocurridoEn es obligatorio");

        if (estadoFinal != State.APTA && estadoFinal != State.NO_APTA) {
            throw new IllegalArgumentException(
                    "estadoFinal debe ser APTA o NO_APTA. Recibido: " + estadoFinal
            );
        }
    }

    // ── DomainEvent contract ─────────────────────────────────────────────────


    public String eventType() {
        return "MUESTRA_EVALUADA";
    }

    // ── Queries de negocio ───────────────────────────────────────────────────

    /** ¿La muestra resultó apta para consumo? */
    public boolean resultadoApto() {
        return estadoFinal == State.APTA;
    }

    /** ¿La muestra requiere proceso de filtrado? */
    public boolean requiereFiltrado() {
        return estadoFinal == State.NO_APTA;
    }

    @Override
    public Instant occurredOn() {
        return null;
    }
}