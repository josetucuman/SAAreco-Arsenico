package com.b2bSolutions.application.ports.in;

import com.b2bsolutions.application.ports.in.command.ReconocerAlertaCommand;

/**
 * Puerto de entrada para reconocer una alerta activa.
 * Solo un operador identificado puede reconocer una alerta.
 */
public interface ReconocerAlertaUseCase {
    void execute(ReconocerAlertaCommand command);
}
