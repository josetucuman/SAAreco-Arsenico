package com.b2bsolutions.domain.transitions.event;

import com.b2bsolutions.domain.state.State;

import java.time.Instant;
import java.util.UUID;

public final class StateTransitionOccurredEvent {

    private final UUID aggregateId;
    private final State from;
    private final State to;
    private final String reason;
    private final String triggeredBy;
    private final Instant occurredAt;

    public StateTransitionOccurredEvent(UUID aggregateId,
                                        State from,
                                        State to,
                                        String reason,
                                        String triggeredBy,
                                        Instant occurredAt) {
        this.aggregateId = aggregateId;
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.triggeredBy = triggeredBy;
        this.occurredAt = occurredAt;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public State getFrom() {
        return from;
    }

    public State getTo() {
        return to;
    }

    public String getReason() {
        return reason;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
