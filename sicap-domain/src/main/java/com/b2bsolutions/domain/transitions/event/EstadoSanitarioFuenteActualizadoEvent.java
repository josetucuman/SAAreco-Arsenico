package com.b2bsolutions.domain.transitions.event;

import com.b2bsolutions.domain.font.enums.EstadoSanitario;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Evento de dominio que se publica cuando el estado sanitario
 * de una FuenteAgua cambia.
 *
 * Es específico del subdominio de fuentes — a diferencia de
 * StateTransitionOccurredEvent que es genérico, este transporta
 * semántica propia de la calidad sanitaria del agua.
 *
 * Casos que lo disparan:
 *   - Muestra evaluada como NO_APTA → fuente pasa a CONTAMINADA
 *   - Proceso de filtrado exitoso   → fuente pasa a APTA
 *   - Inspección manual             → cualquier transición sanitaria
 */
public record EstadoSanitarioFuenteActualizadoEvent(
        UUID fuenteAguaId,
        EstadoSanitario estadoAnterior,
        EstadoSanitario estadoNuevo,
        UUID muestraOrigenId,        // nullable — puede venir de inspección manual
        String motivoCambio,         // nullable — razón del cambio
        String actualizadoPor,       // nullable — operador o SYSTEM
        Instant ocurridoEn
) implements DomainEvent {

    // ── Constructor compacto con validaciones ────────────────────────────────

    public EstadoSanitarioFuenteActualizadoEvent {
        Objects.requireNonNull(fuenteAguaId,   "fuenteAguaId es obligatorio");
        Objects.requireNonNull(estadoAnterior, "estadoAnterior es obligatorio");
        Objects.requireNonNull(estadoNuevo,    "estadoNuevo es obligatorio");
        Objects.requireNonNull(ocurridoEn,     "ocurridoEn es obligatorio");

        if (estadoAnterior == estadoNuevo) {
            throw new IllegalArgumentException(
                    "estadoAnterior y estadoNuevo no pueden ser iguales: " + estadoAnterior
            );
        }

        if (motivoCambio != null && motivoCambio.isBlank()) {
            throw new IllegalArgumentException("motivoCambio no puede ser blank si se provee");
        }
        if (actualizadoPor != null && actualizadoPor.isBlank()) {
            throw new IllegalArgumentException("actualizadoPor no puede ser blank si se provee");
        }
    }

    // ── DomainEvent contract ─────────────────────────────────────────────────

    @Override
    public String eventType() {
        return "ESTADO_SANITARIO_FUENTE_ACTUALIZADO";
    }

    // ── Factory methods semánticos ───────────────────────────────────────────

    /** Fuente contaminada por resultado de muestra */
    public static EstadoSanitarioFuenteActualizadoEvent porMuestraNoApta(
            UUID fuenteAguaId,
            EstadoSanitario estadoAnterior,
            UUID muestraId
    ) {
        return new EstadoSanitarioFuenteActualizadoEvent(
                fuenteAguaId,
                estadoAnterior,
                EstadoSanitario.CONTAMINADA,
                muestraId,
                "Muestra evaluada como NO_APTA",
                "SYSTEM",
                Instant.now()
        );
    }

    /** Fuente rehabilitada luego de proceso de filtrado exitoso */
    public static EstadoSanitarioFuenteActualizadoEvent porFiltradoExitoso(
            UUID fuenteAguaId,
            EstadoSanitario estadoAnterior,
            UUID muestraId
    ) {
        return new EstadoSanitarioFuenteActualizadoEvent(
                fuenteAguaId,
                estadoAnterior,
                EstadoSanitario.APTA,
                muestraId,
                "Proceso de filtrado completado exitosamente",
                "SYSTEM",
                Instant.now()
        );
    }

    /** Cambio manual por operador */
    public static EstadoSanitarioFuenteActualizadoEvent porInspeccionManual(
            UUID fuenteAguaId,
            EstadoSanitario estadoAnterior,
            EstadoSanitario estadoNuevo,
            String operador,
            String motivo
    ) {
        return new EstadoSanitarioFuenteActualizadoEvent(
                fuenteAguaId,
                estadoAnterior,
                estadoNuevo,
                null,
                motivo,
                operador,
                Instant.now()
        );
    }

    // ── Queries de negocio ───────────────────────────────────────────────────

    /** ¿La fuente empeoró su estado sanitario? */
    public boolean esDeterioro() {
        return estadoNuevo == EstadoSanitario.CONTAMINADA
                || estadoNuevo == EstadoSanitario.EN_CUARENTENA;
    }

    /** ¿La fuente mejoró su estado sanitario? */
    public boolean esMejora() {
        return estadoNuevo == EstadoSanitario.APTA;
    }

    /** ¿Fue disparado automáticamente por el sistema? */
    public boolean fueAutomatico() {
        return "SYSTEM".equalsIgnoreCase(actualizadoPor);
    }

    /** ¿Tiene muestra asociada? */
    public boolean tieneMuestraOrigen() {
        return muestraOrigenId != null;
    }

    /** Descripción legible para auditoría */
    public String describir() {
        return "Fuente [" + fuenteAguaId + "]: "
                + estadoAnterior + " → " + estadoNuevo
                + (motivoCambio   != null ? " | Motivo: " + motivoCambio : "")
                + (actualizadoPor != null ? " | Por: "    + actualizadoPor : "");
    }
}