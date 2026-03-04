package com.b2bsolutions.domain.alert;

import com.b2bsolutions.domain.exception.ReglaNegocioException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que representa una alerta generada por el sistema de monitoreo.
 *
 * Ciclo de vida:
 *   activa (acknowledged=false) → reconocida (acknowledged=true)
 *
 * Las alertas CRITICAL requieren reconocimiento explícito de un operador.
 */
public final class Alert {

    private final UUID id;
    private final UUID aggregateId;     // fuente, muestra o planta que la originó
    private final String message;
    private final Severity severity;
    private final Instant raisedAt;     // ← typo corregido: reaisedAt → raisedAt
    private boolean acknowledged;
    private Instant acknowledgedAt;
    private String acknowledgedBy;

    // ── Constructor ──────────────────────────────────────────────────────────

    public Alert(UUID id, UUID aggregateId, String message,
                 Severity severity, Instant raisedAt) {
        this.id          = Objects.requireNonNull(id,          "id es obligatorio");
        this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId es obligatorio");
        this.message     = validarMensaje(message);
        this.severity    = Objects.requireNonNull(severity,    "severity es obligatoria");
        this.raisedAt    = Objects.requireNonNull(raisedAt,    "raisedAt es obligatorio");
        this.acknowledged = false;
    }

    // ── Factory methods semánticos ───────────────────────────────────────────

    public static Alert critica(UUID aggregateId, String message) {
        return new Alert(UUID.randomUUID(), aggregateId, message,
                Severity.CRITICAL, Instant.now());
    }

    public static Alert advertencia(UUID aggregateId, String message) {
        return new Alert(UUID.randomUUID(), aggregateId, message,
                Severity.WARNING, Instant.now());
    }

    public static Alert informativa(UUID aggregateId, String message) {
        return new Alert(UUID.randomUUID(), aggregateId, message,
                Severity.INFO, Instant.now());
    }

    // ── Comportamiento de dominio ────────────────────────────────────────────

    /**
     * Reconoce la alerta — solo un operador identificado puede hacerlo.
     */
    public void acknowledge(String operador) {
        if (operador == null || operador.isBlank()) {
            throw new ReglaNegocioException(
                    "El operador que reconoce la alerta es obligatorio"
            );
        }
        if (this.acknowledged) {
            throw new ReglaNegocioException(
                    "La alerta ya fue reconocida por: " + this.acknowledgedBy
            );
        }
        this.acknowledged    = true;
        this.acknowledgedAt  = Instant.now();
        this.acknowledgedBy  = operador;
    }

    // ── Queries de negocio ───────────────────────────────────────────────────

    public boolean isCritical()      { return severity == Severity.CRITICAL; }
    public boolean isActive()        { return !acknowledged; }
    public boolean isAcknowledged()  { return acknowledged; }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private static String validarMensaje(String message) {
        if (message == null || message.isBlank()) {
            throw new ReglaNegocioException("El mensaje de la alerta es obligatorio");
        }
        return message.trim();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public UUID getId()                  { return id; }
    public UUID getAggregateId()         { return aggregateId; }
    public String getMessage()           { return message; }
    public Severity getSeverity()        { return severity; }
    public Instant getRaisedAt()         { return raisedAt; }
    public boolean isAcknowledged_()     { return acknowledged; }
    public Instant getAcknowledgedAt()   { return acknowledgedAt; }
    public String getAcknowledgedBy()    { return acknowledgedBy; }
}