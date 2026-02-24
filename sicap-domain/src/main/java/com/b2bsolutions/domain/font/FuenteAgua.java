package com.b2bsolutions.domain.font;

import com.b2bsolutions.domain.transitions.event.DomainEvent;
import com.b2bsolutions.domain.transitions.event.EstadoSanitarioFuenteActualizadoEvent;

import java.util.UUID;

public final class FuenteAgua {

    private final UUID id;
    private final String nombre;
    private final TipoFuente tipo;
    private final Ubicacion ubicacion;
    private EstadoSanitario estadoSanitario;

    public FuenteAgua(UUID id, String nombre, TipoFuente tipo, Ubicacion ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }

    public void actualizarEstadoSanitario(EstadoSanitario nuevoEstado) {
        EstadoSanitario anterior = this.estadoSanitario;
        this.estadoSanitario = nuevoEstado;
        DomainEvent.raise(
            new EstadoSanitarioFuenteActualizadoEvent(
                    this.id,
                    anterior,
                    nuevoEstado
            )
        );

    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoFuente getTipo() {
        return tipo;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public EstadoSanitario getEstadoSanitario() {
        return estadoSanitario;
    }
}
