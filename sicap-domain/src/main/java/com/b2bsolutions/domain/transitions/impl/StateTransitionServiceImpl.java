package com.b2bsolutions.domain.transitions.impl;

import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.state.PlantState;
import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.StateTransitionRules;
import com.b2bsolutions.domain.transitions.StateTransitionService;

/**
 * Implementación que delega en StateTransitionRules.
 * No redefine reglas — única fuente de verdad es StateTransitionRules.
 */
public final class StateTransitionServiceImpl implements StateTransitionService {

    // ── MuestraAgua ──────────────────────────────────────────────────────────

    @Override
    public void validate(State from, State to) {
        if (!isAllowed(from, to)) {
            throw new ReglaNegocioException(
                    "Transición de estado inválida: " + from + " → " + to
            );
        }
    }

    @Override
    public boolean isAllowed(State from, State to) {
        if (from == null || to == null) return false;
        return StateTransitionRules.isAllowed(from, to);
    }

    // ── Planta ───────────────────────────────────────────────────────────────

    @Override
    public void validate(PlantState from, PlantState to) {
        if (!isAllowed(from, to)) {
            throw new ReglaNegocioException(
                    "Transición de estado de planta inválida: " + from + " → " + to
            );
        }
    }

    @Override
    public boolean isAllowed(PlantState from, PlantState to) {
        if (from == null || to == null) return false;
        return StateTransitionRules.isAllowed(from, to);
    }
}