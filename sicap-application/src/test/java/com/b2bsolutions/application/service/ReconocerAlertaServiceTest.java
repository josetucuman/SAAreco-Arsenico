package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.command.ReconocerAlertaCommand;
import com.b2bsolutions.application.ports.out.AlertaRepository;
import com.b2bsolutions.domain.alert.Alert;
import com.b2bsolutions.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReconocerAlertaService")
class ReconocerAlertaServiceTest {

    @Mock AlertaRepository alertaRepository;

    ReconocerAlertaService service;

    @BeforeEach
    void setUp() {
        service = new ReconocerAlertaService(alertaRepository);
    }

    @Nested
    @DisplayName("Reconocimiento exitoso")
    class ReconocimientoExitoso {

        @Test
        @DisplayName("Reconoce alerta activa correctamente")
        void reconoce_alerta_activa() {
            // Given
            UUID alertaId = UUID.randomUUID();
            Alert alerta  = Alert.critica(UUID.randomUUID(), "Arsénico elevado");

            when(alertaRepository.findById(alertaId)).thenReturn(Optional.of(alerta));
            when(alertaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            // When
            service.execute(new ReconocerAlertaCommand(alertaId, "operador1"));

            // Then
            assertThat(alerta.isAcknowledged()).isTrue();
            assertThat(alerta.getAcknowledgedBy()).isEqualTo("operador1");
            verify(alertaRepository).save(alerta);
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Lanza excepción cuando la alerta no existe")
        void lanza_excepcion_cuando_alerta_no_existe() {
            // Given
            UUID alertaId = UUID.randomUUID();
            when(alertaRepository.findById(alertaId)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> service.execute(
                    new ReconocerAlertaCommand(alertaId, "operador1")
            )).isInstanceOf(ReglaNegocioException.class);
        }

        @Test
        @DisplayName("Lanza excepción cuando la alerta ya fue reconocida")
        void lanza_excepcion_cuando_alerta_ya_reconocida() {
            // Given
            UUID alertaId = UUID.randomUUID();
            Alert alerta  = Alert.critica(UUID.randomUUID(), "Arsénico elevado");
            alerta.acknowledge("operador1");

            when(alertaRepository.findById(alertaId)).thenReturn(Optional.of(alerta));

            // When / Then
            assertThatThrownBy(() -> service.execute(
                    new ReconocerAlertaCommand(alertaId, "operador2")
            )).isInstanceOf(ReglaNegocioException.class);
        }
    }
}