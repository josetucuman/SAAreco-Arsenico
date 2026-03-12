package com.b2bsolutions.application.services;

import com.b2bsolutions.application.ports.in.RegistrarFuenteAguaUseCase;
import com.b2bsolutions.application.ports.in.command.RegistrarFuenteAguaCommand;
import com.b2bsolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.domain.font.FuenteAgua;
import com.b2bsolutions.domain.font.Ubicacion;
import com.b2bsolutions.domain.font.enums.TipoFuente;
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;

import java.util.UUID;

public class RegistrarFuenteAguaService implements RegistrarFuenteAguaUseCase {

    private final FuenteAguaRepository repository;
    private final DomainEventPublisher eventPublisher;

    public RegistrarFuenteAguaService(FuenteAguaRepository repository,
                                      DomainEventPublisher eventPublisher) {
        this.repository     = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID execute(RegistrarFuenteAguaCommand command) {

        // 1. Crear el aggregate root
        FuenteAgua fuente = FuenteAgua.registrar(
                command.nombre(),
                TipoFuente.valueOf(command.tipo()),
                Ubicacion.de(
                        command.localidad(),
                        command.provincia(),
                        command.latitud(),
                        command.longitud()
                )
        );

        // 2. Persistir
        repository.save(fuente);

        // 3. Publicar eventos
        eventPublisher.publishAll(fuente.pullEventos());

        return fuente.getId();
    }
}