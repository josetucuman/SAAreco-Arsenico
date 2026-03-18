package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.ReconocerAlertaUseCase;
import com.b2bsolutions.application.ports.in.command.ReconocerAlertaCommand;
import  com.b2bsolutions.application.ports.out.AlertaRepository;

public class ReconocerAlertaService implements ReconocerAlertaUseCase {

    private final AlertaRepository alertaRepository;

    public ReconocerAlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Override
    public void execute(ReconocerAlertaCommand command) {

    }
}
