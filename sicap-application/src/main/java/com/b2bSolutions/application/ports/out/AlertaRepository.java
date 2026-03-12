package com.b2bSolutions.application.ports.out;
import com.b2bsolutions.domain.alert.Alert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida (driven port) para persistencia de Alert.
 * La implementación vive en sicap-infrastructure-h2.
 */
public interface AlertaRepository {
    Alert save(Alert alerta);

    Optional<Alert> findById(UUID id);

    /** Retorna solo las alertas no reconocidas — para el dashboard */
    List<Alert> findByAcknowledgedFalse();

    List<Alert> findAll();
}
