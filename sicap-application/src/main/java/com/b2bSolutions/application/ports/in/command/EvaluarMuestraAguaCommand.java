package com.b2bSolutions.application.ports.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Command para evaluar una muestra que está EN_ANALISIS.
 * La evaluación usa los límites OMS encapsulados en NivelArsenico —
 * no se recibe limiteMaximo como parámetro.
 */
public record EvaluarMuestraAguaCommand(

        @NotNull(message = "El ID de la muestra es obligatorio")
        UUID muestraId,

        @NotBlank(message = "El operador es obligatorio")
        String evaluadoPor
) {}