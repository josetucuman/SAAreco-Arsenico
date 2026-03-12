package com.b2bsolutions.application.ports.in;

import com.b2bsolutions.application.ports.in.command.RegistrarFuenteAguaCommand;

import java.util.UUID;

/**
 * Puerto de entrada (driving port) para registrar una nueva fuente de agua.
 * La implementación vive en la capa de servicio.
 */
public interface RegistrarFuenteAguaUseCase {
    /**
     * Registra una nueva fuente de agua.
     * @return UUID de la fuente creada
     */
    UUID execute(RegistrarFuenteAguaCommand command);
}
