package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.EvaluarMuestraAguaUseCase;
import com.b2bsolutions.application.ports.in.command.EvaluarMuestraAguaCommand;
import com.b2bsolutions.application.response.EvaluacionResultado;
import com.b2bSolutions.application.ports.out.MuestraAguaRepository;
import com.b2bSolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.domain.alert.Alert;
import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.font.FuenteAgua;
import com.b2bsolutions.domain.model.MuestraAgua;
import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.StateTransitionService;
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;
import com.b2bSolutions.application.ports.out.AlertaRepository;

public class EvaluarMuestraAguaService implements EvaluarMuestraAguaUseCase {

    private final MuestraAguaRepository muestraRepository;
    private final FuenteAguaRepository fuenteRepository;
    private final StateTransitionService transitionService;
    private final DomainEventPublisher eventPublisher;
    private final AlertaRepository alertaRepository;

    public EvaluarMuestraAguaService(MuestraAguaRepository muestraRepository,
                                     FuenteAguaRepository fuenteRepository,
                                     StateTransitionService transitionService,
                                     DomainEventPublisher eventPublisher, AlertaRepository alertaRepository) {
        this.muestraRepository = muestraRepository;
        this.fuenteRepository = fuenteRepository;
        this.transitionService = transitionService;
        this.eventPublisher = eventPublisher;
        this.alertaRepository = alertaRepository;
    }

    @Override
    public EvaluacionResultado execute(EvaluarMuestraAguaCommand command) {
        // Cargar muestra
        MuestraAgua muestra = muestraRepository.findById(command.muestraId())
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe muestra con ID:\t" + command.muestraId()
                ));

        // 2. Evaluar — transiciona EN_ANALISIS → APTA | NO_APTA
        muestra.evaluar(transitionService);
        muestraRepository.save(muestra);
        eventPublisher.publishAll(muestra.pullEventos());

        // 3. Si NO_APTA → contaminar fuente + generar alerta
        if (muestra.getEstado() == State.NO_APTA) {
            return manejarNoApta(muestra);
        }

        return EvaluacionResultado.apta(muestra.getId());
    }

    private EvaluacionResultado manejarNoApta(MuestraAgua muestra) {

        // 3a. Actualizar estado sanitario de la fuente
        FuenteAgua fuente = fuenteRepository.findById(muestra.getFuenteAguaId())
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe fuente con ID: " + muestra.getFuenteAguaId()
                ));

        fuente.contaminar(muestra.getId());
        fuenteRepository.save(fuente);
        eventPublisher.publishAll(fuente.pullEventos());

        // 3b. Generar alerta crítica
        double arsenicoUgL = muestra.getAnalisisQuimico()
                .getNivelArsenico()
                .getConcentracion()
                .enMicrogramosPorLitro();

        Alert alerta = Alert.critica(
                fuente.getId(),
                "Arsénico: " + arsenicoUgL + " µg/L en " + fuente.getNombre()
                        + " — supera límite OMS ("
                        + muestra.getAnalisisQuimico()
                        .getNivelArsenico()
                        .porcentajeReduccionNecesario()
                        + "% sobre límite)"
        );

        alertaRepository.save(alerta);

        return EvaluacionResultado.noApta(muestra.getId(), alerta.getId());
    }
}
