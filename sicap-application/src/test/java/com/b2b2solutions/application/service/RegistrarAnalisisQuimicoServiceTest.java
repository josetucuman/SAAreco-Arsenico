package com.b2b2solutions.application.service;

import com.b2bsolutions.application.ports.in.command.RegistrarAnalisisQuimicoCommand;
import com.b2bsolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.application.ports.out.MuestraAguaRepository;
import com.b2bsolutions.application.service.RegistrarAnalisisQuimicoService;
import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.font.FuenteAgua;
import com.b2bsolutions.domain.font.Ubicacion;
import com.b2bsolutions.domain.font.enums.TipoFuente;
import com.b2bsolutions.domain.model.MuestraAgua;
import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.StateTransitionService;
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("RegistrarAnalisisQuimicoService")
class RegistrarAnalisisQuimicoServiceTest {

    @Mock MuestraAguaRepository  muestraRepository;
    @Mock FuenteAguaRepository   fuenteRepository;
    @Mock StateTransitionService transitionService;
    @Mock DomainEventPublisher   eventPublisher;

    RegistrarAnalisisQuimicoService service;

    @BeforeEach
    void setUp() {
        service = new RegistrarAnalisisQuimicoService(
                muestraRepository, fuenteRepository,
                transitionService, eventPublisher
        );
    }

    @Test
    @DisplayName("Retorna UUID de la muestra creada cuando la fuente existe")
    void retorna_uuid_muestra_creada() {
        // Given
        UUID fuenteId = UUID.randomUUID();
        when(fuenteRepository.findById(fuenteId)).thenReturn(Optional.of(fuenteExistente(fuenteId)));
        when(muestraRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RegistrarAnalisisQuimicoCommand command = new RegistrarAnalisisQuimicoCommand(
                fuenteId, "Punto A", 8.0, 7.2, 3.0, 18.0
        );

        // When
        UUID muestraId = service.execute(command);

        // Then
        assertThat(muestraId).isNotNull();
        verify(muestraRepository).save(any(MuestraAgua.class));
        verify(eventPublisher).publishAll(any());
    }

    @Test
    @DisplayName("La muestra queda en estado EN_ANALISIS luego del registro")
    void muestra_queda_en_analisis() {
        // Given
        UUID fuenteId = UUID.randomUUID();
        when(fuenteRepository.findById(fuenteId)).thenReturn(Optional.of(fuenteExistente(fuenteId)));
        when(muestraRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RegistrarAnalisisQuimicoCommand command = new RegistrarAnalisisQuimicoCommand(
                fuenteId, "Punto B", 12.0, 7.0, 2.5, 20.0
        );

        // When
        service.execute(command);

        // Then
        verify(muestraRepository).save(argThat(muestra ->
                muestra.getEstado() == State.EN_ANALISIS
        ));
    }

    @Test
    @DisplayName("Lanza excepción cuando la fuente no existe")
    void lanza_excepcion_cuando_fuente_no_existe() {
        // Given
        UUID fuenteId = UUID.randomUUID();
        when(fuenteRepository.findById(fuenteId)).thenReturn(Optional.empty());

        RegistrarAnalisisQuimicoCommand command = new RegistrarAnalisisQuimicoCommand(
                fuenteId, "Punto C", 5.0, 7.0, 1.0, 20.0
        );

        // When / Then
        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining(fuenteId.toString());
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private FuenteAgua fuenteExistente(UUID id) {
        return new FuenteAgua(
                id, "Fuente Test", TipoFuente.POZO,
                Ubicacion.de("Mina Clavero", "Córdoba", -31.72, -64.98)
        );
    }
}