package com.b2bsolutions.application.response;

import com.b2bsolutions.domain.font.enums.EstadoSanitario;
import com.b2bsolutions.domain.font.enums.TipoFuente;

import java.util.UUID;

public record FuenteAguaResponse(
        UUID id,
        String nombre,
        TipoFuente tipo,
        String localidad,
        String provincia,
        double latitud,
        double longitud,
        EstadoSanitario estadoSanitario,
        String estadoDescripcion,    // "Apta para consumo humano"
        boolean aptaParaConsumo,     // para pintar verde/rojo en el front
        String colorHex
) {
}
