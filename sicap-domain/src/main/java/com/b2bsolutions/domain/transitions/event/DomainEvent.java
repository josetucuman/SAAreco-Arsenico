package com.b2bsolutions.domain.transitions.event;

import java.time.Instant;

import static sun.jvmstat.monitor.Units.EVENTS;


public interface DomainEvent {
    Instant occurredOn();

    static void raise(EstadoSanitarioFuenteActualizadoEvent de) {
        EVENTS.get().add(de);
    }
}
