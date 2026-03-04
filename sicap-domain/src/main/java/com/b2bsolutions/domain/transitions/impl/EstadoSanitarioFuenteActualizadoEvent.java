package com.b2bsolutions.domain.transitions.impl;

import com.b2bsolutions.domain.font.enums.EstadoSanitario;
import com.b2bsolutions.domain.transitions.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class EstadoSanitarioFuenteActualizadoEvent implements DomainEvent {
    private final UUID fuenteId;
    private final EstadoSanitario anterior;
    private final EstadoSanitario nuevo;
    private final Instant occurredOn;

    public EstadoSanitarioFuenteActualizadoEvent(UUID fuenteId, EstadoSanitario anterior, EstadoSanitario nuevo, Instant occurredOn) {
        this.fuenteId = fuenteId;
        this.anterior = anterior;
        this.nuevo = nuevo;
        this.occurredOn = occurredOn;
    }

    public UUID fuenteId() {
        return fuenteId;
    }

    public EstadoSanitario anterior() {
        return anterior;
    }

    public EstadoSanitario nuevo() {
        return nuevo;
    }

    @Override
    public Instant occurredOn() {
        return occurredOn;
    }
}
