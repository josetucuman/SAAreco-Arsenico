package com.b2bsolutions.domain.transitions.events;

import com.b2bsolutions.domain.transitions.event.DomainEvent;
import com.b2bsolutions.domain.transitions.event.StateTransitionOccurredEvent;

import java.util.List;

/**
 * Puerto de salida (driven port) para publicación de eventos de dominio.
 *
 * La implementación vive en infraestructura — el dominio solo conoce esta interface.
 * Permite desacoplar completamente la publicación del mecanismo real
 * (memoria, message broker, base de datos, etc.)
 */
public interface DomainEventPublisher {

    /** Publica un único evento */
    void publish(DomainEvent evento);

    /** Publica todos los eventos generados por un Aggregate Root en una operación */
    default void publishAll(List<DomainEvent> eventos) {
        eventos.forEach(this::publish);
    }
}