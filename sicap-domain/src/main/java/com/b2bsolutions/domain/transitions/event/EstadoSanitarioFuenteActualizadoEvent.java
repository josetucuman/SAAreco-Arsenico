package com.b2bsolutions.domain.transitions.event;

import com.b2bsolutions.domain.font.EstadoSanitario;

import java.time.Instant;
import java.util.UUID;

public final class EstadoSanitarioFuenteActualizadoEvent {

    private final UUID fuenteAguaId;
    private final EstadoSanitario anterior;
    private final EstadoSanitario nuevo;
    private final Instant occurredAt;


    public EstadoSanitarioFuenteActualizadoEvent(UUID fuenteAguaId,
                                                 EstadoSanitario anterior,
                                                 EstadoSanitario nuevo) {
        this.fuenteAguaId = fuenteAguaId;
        this.anterior = anterior;
        this.nuevo = nuevo;
        this.occurredAt = Instant.now();
    }

    public UUID getFuenteAguaId() {
        return fuenteAguaId;
    }

    public EstadoSanitario getAnterior() {
        return anterior;
    }

    public EstadoSanitario getNuevo() {
        return nuevo;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
