package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.state.PlantState;
import com.b2bsolutions.domain.state.State;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Define las transiciones válidas para cada máquina de estados del dominio.
 *
 * Dos máquinas de estados independientes:
 *   - PlantState : ciclo de vida de la planta de purificación
 *   - State      : ciclo de vida de una MuestraAgua
 */
public final class StateTransitionRules {

    // ── PlantState ───────────────────────────────────────────────────────────
    private static final Map<PlantState, Set<PlantState>> PLANT_ALLOWED = Map.of(
            PlantState.OFFLINE,       EnumSet.of(PlantState.STARTING, PlantState.MAINTENANCE),
            PlantState.STARTING,      EnumSet.of(PlantState.RUNNING, PlantState.EMERGENCY),
            PlantState.RUNNING,       EnumSet.of(PlantState.DEGRADED, PlantState.SHUTTING_DOWN),
            PlantState.DEGRADED,      EnumSet.of(PlantState.RUNNING, PlantState.EMERGENCY),
            PlantState.EMERGENCY,     EnumSet.of(PlantState.SHUTTING_DOWN),
            PlantState.SHUTTING_DOWN, EnumSet.of(PlantState.OFFLINE),
            PlantState.MAINTENANCE,   EnumSet.of(PlantState.OFFLINE)
    );

    // ── State (MuestraAgua) ──────────────────────────────────────────────────
    private static final Map<State, Set<State>> MUESTRA_ALLOWED = Map.of(
            State.TOMADA,      EnumSet.of(State.EN_ANALISIS, State.DESCARTADA),
            State.EN_ANALISIS, EnumSet.of(State.APTA, State.NO_APTA, State.DESCARTADA),
            State.APTA,        EnumSet.noneOf(State.class),  // estado final
            State.NO_APTA,     EnumSet.noneOf(State.class),  // estado final
            State.DESCARTADA,  EnumSet.noneOf(State.class)   // estado final
    );

    // ── API pública ──────────────────────────────────────────────────────────

    public static boolean isAllowed(PlantState from, PlantState to) {
        return PLANT_ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }

    public static boolean isAllowed(State from, State to) {
        return MUESTRA_ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }

    private StateTransitionRules() {}
}