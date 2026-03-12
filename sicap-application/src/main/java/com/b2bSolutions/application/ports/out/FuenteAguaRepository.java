package com.b2bSolutions.application.ports.out;

import com.b2bsolutions.domain.font.FuenteAgua;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FuenteAguaRepository {
    FuenteAgua save(FuenteAgua fuenteAgua);

    Optional<FuenteAgua> findById(UUID id);

    List<FuenteAgua> findAll();

    boolean existsById(UUID id);
}
