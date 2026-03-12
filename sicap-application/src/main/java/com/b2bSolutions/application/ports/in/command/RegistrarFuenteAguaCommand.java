package com.b2bSolutions.application.ports.in.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Command para registrar una nueva fuente de agua.
 * Driving port — viene del controller REST.
 */
public record RegistrarFuenteAguaCommand(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotNull(message = "El tipo de fuente es obligatorio")
        String tipo,

        @NotBlank(message = "La localidad es obligatoria")
        String localidad,

        @NotBlank(message = "La provincia es obligatoria")
        String provincia,

        @NotNull(message = "La latitud es obligatoria")
        Double latitud,

        @NotNull(message = "La longitud es obligatoria")
        Double longitud
) {}