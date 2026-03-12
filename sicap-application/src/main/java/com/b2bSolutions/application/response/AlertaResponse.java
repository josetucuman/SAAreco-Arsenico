package com.b2bSolutions.application.response;

import com.b2bsolutions.domain.alert.Severity;

import java.time.Instant;
import java.util.UUID;

public record AlertaResponse(
        UUID id,
        UUID aggregateId,
        String message,
        Severity severity,
        String severityColor,       // para el front: #EF4444, #F59E0B, #3B82F6
        boolean acknowledged,
        Instant raisedAt,
        String acknowledgedBy,      // null si no fue reconocida
        Instant acknowledgedAt
) {
}
