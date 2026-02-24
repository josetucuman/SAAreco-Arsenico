package com.b2bsolutions.domain.transitions;



import com.b2bsolutions.domain.state.PlantState;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class StateTransitionRules {

    private static final Map<PlantState, Set<PlantState>> ALLOWED =  Map.of(
            PlantState.OFFLINE, EnumSet.of(PlantState.STARTING, PlantState.MAINTENANCE),
            PlantState.STARTING, EnumSet.of(PlantState.RUNNING, PlantState.EMERGENCY),
            PlantState.RUNNING, EnumSet.of(PlantState.DEGRADED, PlantState.SHUTTING_DOWN),
            PlantState.DEGRADED, EnumSet.of(PlantState.RUNNING, PlantState.EMERGENCY),
            PlantState.EMERGENCY, EnumSet.of(PlantState.SHUTTING_DOWN),
            PlantState.SHUTTING_DOWN, EnumSet.of(PlantState.OFFLINE),
            PlantState.MAINTENANCE, EnumSet.of(PlantState.OFFLINE)
    );

    public static boolean isAllowed(PlantState from, PlantState to) {
        return ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }

    private StateTransitionRules(){}

}
