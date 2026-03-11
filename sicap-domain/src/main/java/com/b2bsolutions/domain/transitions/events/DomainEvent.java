package com.b2bsolutions.domain.transitions.events;

import java.time.Instant;

/**
 * Contrato base para todos los eventos de dominio.
 *
 * Reglas:
 *   - Esta interface no conoce infraestructura
 *   - La publicación es responsabilidad de DomainEventPublisher
 *   - Nunca agregar lógica de publicación aquí
 */
public interface DomainEvent {

    /** Tipo del evento — identifica de qué evento se trata */
    String eventType();

    /** Momento exacto en que ocurrió el evento */
    Instant ocurridoEn();
}