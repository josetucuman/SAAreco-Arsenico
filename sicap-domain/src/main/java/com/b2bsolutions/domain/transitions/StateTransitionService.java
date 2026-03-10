package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.state.PlantState;
import com.b2bsolutions.domain.state.State;

public interface StateTransitionService {

    // ── MuestraAgua ──────────────────────────────────────────────────────────
    void validate(State from, State to);
    boolean isAllowed(State from, State to);

    // ── Planta ───────────────────────────────────────────────────────────────
    void validate(PlantState from, PlantState to);
    boolean isAllowed(PlantState from, PlantState to);
}