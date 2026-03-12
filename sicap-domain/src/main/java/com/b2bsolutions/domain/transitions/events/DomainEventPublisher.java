package com.b2bsolutions.domain.transitions.events;

import java.util.List;

/**
 * Puerto de salida (driven port) para publicación de eventos de dominio.
 *
 * Reglas:
 *   - La implementación vive en infraestructura — el dominio solo conoce esta interface
 *   - Nunca inyectar dependencias de Spring, Kafka o cualquier framework aquí
 *   - El dominio genera eventos, la infraestructura los publica
 */
public interface DomainEventPublisher {

    /** Publica un único evento */
    void publish(DomainEvent evento);

    /** Publica todos los eventos generados por un Aggregate Root en una operación */
    default void publishAll(List<DomainEvent> eventos) {
        eventos.forEach(this::publish);
    }
}