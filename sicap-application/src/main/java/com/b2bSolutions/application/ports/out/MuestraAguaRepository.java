package com.b2bSolutions.application.ports.out;

import com.b2bsolutions.domain.model.MuestraAgua;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida (driven port) para persistencia de MuestraAgua.
 * La implementación vive en sicap-infrastructure-h2.
 */
public interface MuestraAguaRepository {
    MuestraAgua save(MuestraAgua muestra);

    Optional<MuestraAgua> findById(UUID id);

    List<MuestraAgua> findByFuenteAguaId(UUID fuenteAguaId);
}
