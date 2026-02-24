package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.state.PlantState;

import java.time.Instant;

public final class StateTransition {
    private final PlantState from;
    private final PlantState to;
    private final String reason;
    private final String triggeredBy;
    private final Instant occurredAt;

    public StateTransition(PlantState from,
                           PlantState to,
                           String reason,
                           String triggeredBy,
                           Instant occurredAt) {
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.triggeredBy = triggeredBy;
        this.occurredAt = occurredAt;
    }

    public PlantState from() { return from; }
    public PlantState to() { return to; }
    public String reason() { return reason; }
    public String triggeredBy() { return triggeredBy; }
    public Instant occurredAt() { return occurredAt; }

}
