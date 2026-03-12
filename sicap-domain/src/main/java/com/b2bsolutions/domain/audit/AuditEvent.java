package com.b2bsolutions.domain.audit;

import com.b2bsolutions.domain.audit.enums.AuditAction;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Registro inmutable de una acción ocurrida en el sistema.
 * Se persiste para trazabilidad completa de operaciones.
 */
public record AuditEvent(
        UUID id,
        UUID aggregateId,       // entidad afectada
        AuditAction action,     // ← enum tipado, no String libre
        String performedBy,     // operador o "SYSTEM"
        Instant performedAt,
        String details          // nullable — contexto adicional
) {

    public AuditEvent {
        Objects.requireNonNull(id,          "id es obligatorio");
        Objects.requireNonNull(aggregateId, "aggregateId es obligatorio");
        Objects.requireNonNull(action,      "action es obligatoria");
        Objects.requireNonNull(performedAt, "performedAt es obligatorio");

        if (performedBy == null || performedBy.isBlank()) {
            throw new IllegalArgumentException("performedBy es obligatorio");
        }
    }

    // ── Factory method ───────────────────────────────────────────────────────


    public static AuditEvent of(UUID aggregateId,  AuditAction  action,
                                String performedBy, String details) {
        return new AuditEvent(
                UUID.randomUUID(),
                aggregateId,
                action,
                performedBy,
                Instant.now(),
                details
        );
    }

    public boolean fueAutomatico() {
        return "SYSTEM".equalsIgnoreCase(performedBy);
    }
}