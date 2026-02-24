package com.b2bsolutions.domain.transitions.impl;

import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.StateTransitionService;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class StateTransitionServiceImpl implements StateTransitionService {

    private final Map<State, Set<State>> allowedTransitions =
            new EnumMap<>(State.class);

    public StateTransitionServiceImpl() {
        initRules();
    }

    private void initRules() {

        allowedTransitions.put(
                State.TOMADA,
                EnumSet.of(State.EN_ANALISIS)
        );

        allowedTransitions.put(
                State.EN_ANALISIS,
                EnumSet.of(State.APTA, State.NO_APTA)
        );

        allowedTransitions.put(
                State.NO_APTA,
                EnumSet.of(State.DESCARTADA)
        );

        allowedTransitions.put(
                State.APTA,
                EnumSet.noneOf(State.class)
        );

        allowedTransitions.put(
                State.DESCARTADA,
                EnumSet.noneOf(State.class)
        );
    }

    @Override
    public void validate(State from, State to) {

        if (!isAllowed(from, to)) {
            throw new ReglaNegocioException(
                    "Transición de estado inválida: " +
                            from + " -> " + to
            );
        }
    }

    @Override
    public boolean isAllowed(State from, State to) {
            if (from == null || to == null) {
                return false;
            }
        return allowedTransitions
                .getOrDefault(from, Set.of())
                .contains(to);
    }
}
