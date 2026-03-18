package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.FuenteAguaQueryUseCase;
import com.b2bsolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.application.response.FuenteAguaResponse;
import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.font.FuenteAgua;

import java.util.List;
import java.util.UUID;

public class FuenteAguaQueryService implements FuenteAguaQueryUseCase {

    private final FuenteAguaRepository repository;

    public FuenteAguaQueryService(FuenteAguaRepository repository) {
        this.repository = repository;
    }

    @Override
    public FuenteAguaResponse findById(UUID id) {
        FuenteAgua fuente = repository.findById(id)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe fuente con ID: " + id
                ));
        return toResponse(fuente);
    }

    @Override
    public List<FuenteAguaResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FuenteAguaResponse toResponse(FuenteAgua fuente) {
        return new FuenteAguaResponse(
                fuente.getId(),
                fuente.getNombre(),
                fuente.getTipo(),
                fuente.getUbicacion().getLocalidad(),
                fuente.getUbicacion().getProvincia(),
                fuente.getUbicacion().getLatitud(),
                fuente.getUbicacion().getLongitud(),
                fuente.getEstadoSanitario(),
                fuente.getEstadoSanitario().getDescripcion(),
                fuente.getEstadoSanitario().isAptaParaConsumo(),
                fuente.getEstadoSanitario().getColorHex()
        );
    }
}