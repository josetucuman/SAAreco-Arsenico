package com.b2bsolutions.domain.transitions.event;

import java.time.Instant;

/**
 * Contrato base para todos los eventos de dominio.
 *
 * Regla: esta interface no conoce infraestructura.
 * La publicación es responsabilidad de DomainEventPublisher
 * en la capa de aplicación — nunca del dominio mismo.
 */
public interface DomainEvent {

    /** Tipo del evento — identifica de qué evento se trata */
    String eventType();

    /** Momento exacto en que ocurrió el evento */
    Instant ocurridoEn();
}