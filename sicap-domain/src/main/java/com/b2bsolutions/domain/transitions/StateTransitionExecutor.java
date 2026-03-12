package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.state.State;
<<<<<<< HEAD
import com.b2bsolutions.domain.transitions.events.StateTransitionOccurredEvent;
=======
import com.b2bsolutions.domain.transitions.events.DomainEvent;
>>>>>>> b36d486976ff02c56e0a1e16c683981c6a998a3b
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;
import com.b2bsolutions.domain.transitions.events.StateTransitionOccurredEvent;

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

        DomainEvent event = new StateTransitionOccurredEvent(
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