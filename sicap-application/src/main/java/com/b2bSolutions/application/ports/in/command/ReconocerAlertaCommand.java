package com.b2bsolutions.application.ports.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Command para reconocer una alerta activa.
 * Solo un operador identificado puede reconocer una alerta.
 */
public record ReconocerAlertaCommand(

        @NotNull(message = "El ID de la alerta es obligatorio")
        UUID alertaId,

        @NotBlank(message = "El operador que reconoce es obligatorio")
        String reconocidoPor
) {}
