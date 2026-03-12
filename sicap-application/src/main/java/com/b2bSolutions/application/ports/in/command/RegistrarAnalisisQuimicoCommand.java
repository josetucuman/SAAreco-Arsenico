package com.b2bSolutions.application.ports.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * Command para registrar el análisis químico de una muestra.
 * Contiene todos los parámetros fisicoquímicos medidos.
 */
public record RegistrarAnalisisQuimicoCommand(

        @NotNull(message = "El ID de la fuente es obligatorio")
        UUID fuenteAguaId,

        @NotBlank(message = "El punto de origen es obligatorio")
        String puntoOrigen,

        // Arsénico en µg/L
        @NotNull
        @Positive(message = "El arsénico no puede ser negativo")
        Double arsenicoUgL,

        @NotNull
        Double ph,

        @NotNull
        @Positive
        Double turbidezNTU,

        @NotNull
        Double temperaturaC
) {}