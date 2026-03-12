package com.b2bSolutions.application.response;

import com.b2bsolutions.domain.state.State;

import java.util.UUID;

/**
 * Resultado de la evaluación de una muestra.
 * El controller decide el HTTP status basándose en estos campos.
 */
public record EvaluacionResultado(
        UUID muestraId,
        State estadoFinal,
        boolean aptaParaConsumo,
        boolean alertaGenerada,
        UUID alertaId,        // null si no se generó alerta
        String descripcion
) {

    public static EvaluacionResultado apta(UUID muestraId) {
        return new EvaluacionResultado(
                muestraId,
                State.APTA,
                true,
                false,
                null,
                "Muestra evaluada como APTA. Dentro de límites OMS."
        );
    }

    public static EvaluacionResultado noApta(UUID muestraId, UUID alertaId) {
        return new EvaluacionResultado(
                muestraId,
                State.NO_APTA,
                false,
                true,
                alertaId,
                "Muestra evaluada como NO APTA. Alerta generada."
        );
    }
}
