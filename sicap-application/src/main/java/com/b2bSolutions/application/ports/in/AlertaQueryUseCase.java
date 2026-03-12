package com.b2bsolutions.application.ports.in;


/**
 * Puerto de entrada para consultas de Alertas.
 * Solo lectura — no modifica estado del dominio.
 */
public interface AlertaQueryUseCase {
    /** Retorna solo las alertas no reconocidas */
    List<AlertaResponse> findActivas();

    /** Retorna todas las alertas — activas e históricas */
    List<AlertaResponse> findAll();
}
