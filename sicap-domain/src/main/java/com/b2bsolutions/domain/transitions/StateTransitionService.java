package com.b2bsolutions.domain.transitions;

import com.b2bsolutions.domain.state.State;

public interface StateTransitionService {
    void validate(State from, State to);

    boolean isAllowed(State from, State to);
}
