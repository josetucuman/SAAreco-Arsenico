package com.b2bsolutions.domain.audit;

import java.time.Instant;

public record AuditEvent(
        String action,
        String performedBy,
        Instant performedAt,
        String details
) {
}
