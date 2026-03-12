package com.b2bsolutions.domain.transitions.events;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Evento de dominio que se publica cuando una MuestraAgua
 * transiciona TOMADA → EN_ANALISIS.
 */
public record MuestraEnAnalisisEvent(
        UUID muestraId,
        UUID fuenteAguaId,
        Instant ocurridoEn
) implements DomainEvent {

    public MuestraEnAnalisisEvent {
        Objects.requireNonNull(muestraId,    "muestraId es obligatorio");
        Objects.requireNonNull(fuenteAguaId, "fuenteAguaId es obligatorio");
        Objects.requireNonNull(ocurridoEn,   "ocurridoEn es obligatorio");
    }

    @Override
    public String eventType() {
        return "MUESTRA_EN_ANALISIS";
    }
}