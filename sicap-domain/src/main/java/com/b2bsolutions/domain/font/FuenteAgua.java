package com.b2bsolutions.domain.font;

import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.font.enums.EstadoSanitario;
import com.b2bsolutions.domain.font.enums.TipoFuente;
import com.b2bsolutions.domain.transitions.events.DomainEvent;
import com.b2bsolutions.domain.transitions.events.EstadoSanitarioFuenteActualizadoEvent;

import java.time.Instant;
import java.util.*;

public final class FuenteAgua {

    private final UUID id;
    private final String nombre;
    private final TipoFuente tipo;
    private final Ubicacion ubicacion;
    private EstadoSanitario estadoSanitario;
    private final List<DomainEvent> eventos = new ArrayList<>();

    public FuenteAgua(UUID id, String nombre, TipoFuente tipo, Ubicacion ubicacion) {
        this.id = Objects.requireNonNull(id, "El ID es obligatorio");
        this.nombre = validarNombre(nombre);
        this.tipo          = Objects.requireNonNull(tipo,      "El tipo de fuente es obligatorio");
        this.ubicacion     = Objects.requireNonNull(ubicacion, "La ubicación es obligatoria");
        this.estadoSanitario = EstadoSanitario.DESCONOCIDO; // ← estado inicial explícito
    }

    public static  FuenteAgua registrar(String nombre,
                                        TipoFuente tipo, Ubicacion ubicacion){
        return (new FuenteAgua(UUID.randomUUID(), nombre, tipo, ubicacion));
    }

    public void actualizarEstadoSanitario(EstadoSanitario nuevoEstado) {
        Objects.requireNonNull(nuevoEstado, "El nuevo estado sanitario es obligatorio");
        if(this.estadoSanitario == nuevoEstado){
            throw new ReglaNegocioException("La fuente ya se encuentra en estado "+nuevoEstado);
        }

        EstadoSanitario anterior = this.estadoSanitario;
        this.estadoSanitario = nuevoEstado;

        eventos.add(new EstadoSanitarioFuenteActualizadoEvent(
                this.id,
                anterior,
                nuevoEstado,
                null,
                null,
                "SYSTEM",
                Instant.now()
        ));

    }

    public void contaminar(UUID muestraID){
        EstadoSanitario anterior = this.estadoSanitario;
        this.estadoSanitario = EstadoSanitario.CONTAMINADA;
        eventos.add(EstadoSanitarioFuenteActualizadoEvent.porMuestraNoApta(
                this.id,
                anterior,
                muestraID
        ));
    }

    public void rehabilitar(UUID muestraId){

        EstadoSanitario anterior = this.estadoSanitario;
        this.estadoSanitario = EstadoSanitario.APTA;
        eventos.add(EstadoSanitarioFuenteActualizadoEvent.porFiltradoExitoso(
                this.id, anterior, muestraId));
    }


    //Igual que en NuestaAgua, el servicio llama a esto despues de cada operacion
    public List<DomainEvent> pullEventos() {
         List<DomainEvent> copia = Collections.unmodifiableList(new ArrayList<>(eventos));
         eventos.clear();
         return copia;
    }


    public boolean estaApta() {
        return estadoSanitario == EstadoSanitario.APTA;
    }

    public boolean estaContaminada() {
        return estadoSanitario == EstadoSanitario.CONTAMINADA;
    }

    private static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaNegocioException("El nombre de la fuente es obligatorio");
        }
        return nombre.trim();
    }
    public boolean requiereEvaluacion() {
        return estadoSanitario == EstadoSanitario.DESCONOCIDO
                || estadoSanitario == EstadoSanitario.EN_CUARENTENA;
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
