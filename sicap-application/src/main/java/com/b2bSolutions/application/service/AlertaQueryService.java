package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.AlertaQueryUseCase;
import com.b2bsolutions.application.ports.out.AlertaRepository;
import com.b2bsolutions.application.response.AlertaResponse;
import com.b2bsolutions.domain.alert.Alert;

import java.util.List;

public class AlertaQueryService implements AlertaQueryUseCase {

    private final AlertaRepository repository;

    public AlertaQueryService(AlertaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AlertaResponse> findActivas() {
        return repository.findByAcknowledgedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<AlertaResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AlertaResponse toResponse(Alert alerta) {
        return new AlertaResponse(
                alerta.getId(),
                alerta.getAggregateId(),
                alerta.getMessage(),
                alerta.getSeverity(),
                alerta.getSeverity().getColorHex(),
                alerta.isAcknowledged(),
                alerta.getRaisedAt(),
                alerta.getAcknowledgedBy(),
                alerta.getAcknowledgedAt()
        );
    }
}