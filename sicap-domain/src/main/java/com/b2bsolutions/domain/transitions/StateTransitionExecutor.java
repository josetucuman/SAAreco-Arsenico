package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.event.StateTransitionOccurredEvent;
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class StateTransitionExecutor {

    private final StateTransitionService transitionService;
    private final DomainEventPublisher eventPublisher;

    public StateTransitionExecutor(StateTransitionService transitionService,
                                   DomainEventPublisher eventPublisher) {
        this.transitionService = Objects.requireNonNull(transitionService, "transitionService es obligatorio");
        this.eventPublisher    = Objects.requireNonNull(eventPublisher,    "eventPublisher es obligatorio");
    }

    public State execute(
            UUID aggregateId,
            State currentState,
            State nextState,
            String reason,
            String triggeredBy
    ) {
        Objects.requireNonNull(aggregateId,  "aggregateId es obligatorio");
        Objects.requireNonNull(currentState, "currentState es obligatorio");
        Objects.requireNonNull(nextState,    "nextState es obligatorio");

        transitionService.validate(currentState, nextState);

        StateTransitionOccurredEvent event = new StateTransitionOccurredEvent(
                aggregateId,
                currentState,
                nextState,
                reason,
                triggeredBy,
                Instant.now()
        );

        eventPublisher.publish(event);

        return nextState;
    }
}