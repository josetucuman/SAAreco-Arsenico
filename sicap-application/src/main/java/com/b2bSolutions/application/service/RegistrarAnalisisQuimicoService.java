package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.RegistrarAnalisisQuimicoUseCase;
import com.b2bsolutions.application.ports.in.command.RegistrarAnalisisQuimicoCommand;
import com.b2bsolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.application.ports.out.MuestraAguaRepository;
import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.model.AnalisisQuimico;
import com.b2bsolutions.domain.model.MuestraAgua;
import com.b2bsolutions.domain.model.NivelArsenico;
import com.b2bsolutions.domain.model.ParametrosCalidad;
import com.b2bsolutions.domain.transitions.StateTransitionService;
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;

import java.time.Instant;
import java.util.UUID;

public class RegistrarAnalisisQuimicoService implements RegistrarAnalisisQuimicoUseCase {

    private final MuestraAguaRepository muestraRepository;
    private final FuenteAguaRepository  fuenteRepository;
    private final StateTransitionService transitionService;
    private final DomainEventPublisher   eventPublisher;

    public RegistrarAnalisisQuimicoService(MuestraAguaRepository muestraRepository,
                                           FuenteAguaRepository fuenteRepository,
                                           StateTransitionService transitionService,
                                           DomainEventPublisher eventPublisher) {
        this.muestraRepository  = muestraRepository;
        this.fuenteRepository   = fuenteRepository;
        this.transitionService  = transitionService;
        this.eventPublisher     = eventPublisher;
    }

    @Override
    public UUID execute(RegistrarAnalisisQuimicoCommand command) {

        // 1. Verificar que la fuente existe
        fuenteRepository.findById(command.fuenteAguaId())
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe fuente con ID: " + command.fuenteAguaId()
                ));

        // 2. Crear la muestra
        MuestraAgua muestra = MuestraAgua.tomar(
                command.fuenteAguaId(),
                command.puntoOrigen()
        );

        // 3. Construir el análisis con Value Objects
        AnalisisQuimico analisis = new AnalisisQuimico(
                NivelArsenico.deMicrogramosPorLitro(command.arsenicoUgL()),
                ParametrosCalidad.de(
                        command.ph(),
                        command.turbidezNTU(),
                        command.temperaturaC()
                ),
                Instant.now()
        );

        // 4. Registrar análisis — transiciona TOMADA → EN_ANALISIS
        muestra.registrarAnalisis(analisis, transitionService);

        // 5. Persistir y publicar eventos
        muestraRepository.save(muestra);
        eventPublisher.publishAll(muestra.pullEventos());

        return muestra.getId();
    }
}