package com.b2bsolutions.domain.alert;

import java.time.Instant;
import java.util.UUID;

public final class Alert {
    private final UUID idAlert;
    private final String message;
    private final Severity severity;
    private final Instant reaisedAt;
    private boolean acknowledged;

    public Alert(UUID idAlert, String message, Severity severity, Instant reaisedAt) {
        this.idAlert = idAlert;
        this.message = message;
        this.severity = severity;
        this.reaisedAt = reaisedAt;
    }

    public void acnowledge(){
        this.acknowledged = true;
    }

    public boolean isCritical() {
        return severity == Severity.CRITICAL;
    }
}
