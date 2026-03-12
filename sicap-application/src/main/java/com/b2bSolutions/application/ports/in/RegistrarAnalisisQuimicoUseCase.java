package com.b2bSolutions.application.ports.in;

import com.b2bsolutions.application.ports.in.command.RegistrarAnalisisQuimicoCommand;

import java.util.UUID;

/**
 * Puerto de entrada para registrar el análisis químico de una muestra.
 * Crea la muestra y la deja en estado EN_ANALISIS.
 */
public interface RegistrarAnalisisQuimicoUseCase {
    /**
     * Crea la muestra, registra el análisis y la deja en EN_ANALISIS.
     * @return UUID de la muestra creada
     */

    UUID execute(RegistrarAnalisisQuimicoCommand command);
}
