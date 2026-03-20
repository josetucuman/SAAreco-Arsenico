package com.b2b2solutions.application.service;

import com.b2bsolutions.application.ports.out.AlertaRepository;
import com.b2bsolutions.application.response.AlertaResponse;
import com.b2bsolutions.application.service.AlertaQueryService;
import com.b2bsolutions.domain.alert.Alert;
import com.b2bsolutions.domain.alert.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertaQueryService")
class AlertaQueryServiceTest {

    @Mock AlertaRepository repository;

    AlertaQueryService service;

    @BeforeEach
    void setUp() {
        service = new AlertaQueryService(repository);
    }

    @Test
    @DisplayName("Retorna solo alertas no reconocidas")
    void retorna_solo_alertas_no_reconocidas() {
        // Given
        Alert alerta1 = Alert.critica(UUID.randomUUID(), "Arsénico elevado norte");
        Alert alerta2 = Alert.critica(UUID.randomUUID(), "Arsénico elevado sur");

        when(repository.findByAcknowledgedFalse()).thenReturn(List.of(alerta1, alerta2));

        // When
        List<AlertaResponse> responses = service.findActivas();

        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses).allMatch(r -> !r.acknowledged());
    }

    @Test
    @DisplayName("Retorna lista vacía cuando no hay alertas activas")
    void retorna_lista_vacia_cuando_no_hay_activas() {
        // Given
        when(repository.findByAcknowledgedFalse()).thenReturn(List.of());

        // When
        List<AlertaResponse> responses = service.findActivas();

        // Then
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("Retorna todas las alertas incluyendo reconocidas")
    void retorna_todas_las_alertas() {
        // Given
        Alert alerta1 = Alert.critica(UUID.randomUUID(), "Arsénico elevado");
        Alert alerta2 = Alert.critica(UUID.randomUUID(), "Turbiedad alta");
        alerta2.acknowledge("operador1");

        when(repository.findAll()).thenReturn(List.of(alerta1, alerta2));

        // When
        List<AlertaResponse> responses = service.findAll();

        // Then
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("El response incluye el color de severidad")
    void response_incluye_color_severidad() {
        // Given
        Alert alerta = Alert.critica(UUID.randomUUID(), "Arsénico elevado");
        when(repository.findAll()).thenReturn(List.of(alerta));

        // When
        List<AlertaResponse> responses = service.findAll();

        // Then
        assertThat(responses.get(0).severity()).isEqualTo(Severity.CRITICAL);
        assertThat(responses.get(0).severityColor()).isNotBlank();
    }
}