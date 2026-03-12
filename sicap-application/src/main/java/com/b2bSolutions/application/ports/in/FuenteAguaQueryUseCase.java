package com.b2bsolutions.application.ports.in;

import java.util.UUID;
import java.util.List;
import com.b2bsolutions.application.response.FuenteAguaResponse;

/**
 * Puerto de entrada para consultas de FuenteAgua.
 * Solo lectura — no modifica estado del dominio.
 */
public interface FuenteAguaQueryUseCase {
    FuenteAguaResponse findById(UUID id);

    List<FuenteAguaResponse> findAll();
}
