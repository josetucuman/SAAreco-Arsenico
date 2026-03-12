package com.b2bsolutions.application.ports.in;


import com.b2bSolutions.application.response.EvaluacionResultado;
import com.b2bsolutions.application.ports.in.command.EvaluarMuestraAguaCommand;

/**
 * Puerto de entrada para evaluar una muestra que está EN_ANALISIS.
 * Evalúa contra límites OMS, actualiza estado sanitario de la fuente
 * y genera alerta si supera límites.
 */
public interface EvaluarMuestraAguaUseCase {
    /**
     * @return resultado con estado final y si generó alerta
     */
    EvaluacionResultado execute(EvaluarMuestraAguaCommand command);
}
