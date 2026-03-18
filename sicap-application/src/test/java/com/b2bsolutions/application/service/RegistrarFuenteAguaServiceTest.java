package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.command.RegistrarFuenteAguaCommand;
import com.b2bsolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.domain.font.FuenteAgua;
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarFuenteAguaService")
class RegistrarFuenteAguaServiceTest {

    @Mock FuenteAguaRepository repository;
    @Mock DomainEventPublisher  eventPublisher;

    RegistrarFuenteAguaService service;

    @BeforeEach
    void setUp() {
        service = new RegistrarFuenteAguaService(repository, eventPublisher);
    }

    @Nested
    @DisplayName("Registro exitoso")
    class RegistroExitoso {

        @Test
        @DisplayName("Retorna UUID de la fuente creada")
        void retorna_uuid_de_fuente_creada() {
            // Given
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            RegistrarFuenteAguaCommand command = new RegistrarFuenteAguaCommand(
                    "Fuente Norte",
                    "POZO",
                    "Mina Clavero",
                    "Córdoba",
                    -31.72,
                    -64.98
            );

            // When
            UUID id = service.execute(command);

            // Then
            assertThat(id).isNotNull();
            verify(repository).save(any(FuenteAgua.class));
            verify(eventPublisher).publishAll(any());
        }

        @Test
        @DisplayName("Persiste la fuente con estado inicial DESCONOCIDO")
        void persiste_fuente_con_estado_inicial_desconocido() {
            // Given
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            RegistrarFuenteAguaCommand command = new RegistrarFuenteAguaCommand(
                    "Fuente Sur",
                    "SUPERFICIAL",
                    "Villa Dolores",
                    "Córdoba",
                    -31.94,
                    -65.19
            );

            // When
            service.execute(command);

            // Then
            verify(repository).save(argThat(fuente ->
                    fuente.getNombre().equals("Fuente Sur") &&
                            fuente.getEstadoSanitario().name().equals("DESCONOCIDO")
            ));
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Lanza excepción cuando el tipo de fuente es inválido")
        void lanza_excepcion_cuando_tipo_invalido() {
            // Given
            RegistrarFuenteAguaCommand command = new RegistrarFuenteAguaCommand(
                    "Fuente Test",
                    "TIPO_INVALIDO",
                    "Localidad",
                    "Provincia",
                    -31.0,
                    -64.0
            );

            // When / Then
            assertThatThrownBy(() -> service.execute(command))
                    .isInstanceOf(Exception.class);
        }
    }
}