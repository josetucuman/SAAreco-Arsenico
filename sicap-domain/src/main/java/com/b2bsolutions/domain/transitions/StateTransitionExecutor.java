package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.event.DomainEventPublisher;
import com.b2bsolutions.domain.transitions.event.StateTransitionOccurredEvent;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public final class StateTransitionExecutor {

    private final StateTransitionService transitionService;
    private final DomainEventPublisher eventPublisher;

    public StateTransitionExecutor(StateTransitionService transitionService,
                                   DomainEventPublisher eventPublisher) {
        this.transitionService = transitionService;
        this.eventPublisher = eventPublisher;
    }

    public State execute(
            UUID aggregateId,
            State currentState,
            State nextState,
            String reason,
            String triggeredBy
    ){
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
