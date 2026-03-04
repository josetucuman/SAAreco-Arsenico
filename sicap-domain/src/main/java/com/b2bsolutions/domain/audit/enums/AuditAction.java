package com.b2bsolutions.domain.audit.enums;

/**
 * Acciones auditables del sistema.
 */
public enum AuditAction {

    // Fuente de agua
    FUENTE_REGISTRADA,
    FUENTE_ESTADO_SANITARIO_ACTUALIZADO,

    // Muestras
    MUESTRA_TOMADA,
    MUESTRA_ANALISIS_REGISTRADO,
    MUESTRA_EVALUADA,
    MUESTRA_DESCARTADA,

    // Alertas
    ALERTA_GENERADA,
    ALERTA_RECONOCIDA,

    // Planta
    PLANTA_ESTADO_CAMBIADO,

    // Procesos
    PROCESO_FILTRADO_INICIADO,
    PROCESO_FILTRADO_COMPLETADO,
    PROCESO_FILTRADO_FALLIDO
}