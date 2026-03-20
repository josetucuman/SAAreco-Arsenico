package com.b2bsolutions.application.service;

import com.b2bsolutions.application.ports.in.command.EvaluarMuestraAguaCommand;
import com.b2bsolutions.application.ports.out.AlertaRepository;
import com.b2bsolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.application.ports.out.MuestraAguaRepository;
import com.b2bsolutions.application.response.EvaluacionResultado;
import com.b2bsolutions.domain.alert.Alert;
import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.font.FuenteAgua;
import com.b2bsolutions.domain.font.Ubicacion;
import com.b2bsolutions.domain.font.enums.TipoFuente;
import com.b2bsolutions.domain.model.AnalisisQuimico;
import com.b2bsolutions.domain.model.MuestraAgua;
import com.b2bsolutions.domain.model.NivelArsenico;
import com.b2bsolutions.domain.model.ParametrosCalidad;
import com.b2bsolutions.domain.state.State;
import com.b2bsolutions.domain.transitions.StateTransitionService;
import com.b2bsolutions.domain.transitions.events.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EvaluarMuestraAguaService")
class EvaluarMuestraAguaServiceTest {

    @Mock MuestraAguaRepository  muestraRepository;
    @Mock FuenteAguaRepository   fuenteRepository;
    @Mock AlertaRepository       alertaRepository;
    @Mock StateTransitionService transitionService;
    @Mock DomainEventPublisher   eventPublisher;

    EvaluarMuestraAguaService service;

    @BeforeEach
    void setUp() {
        service = new EvaluarMuestraAguaService(
                muestraRepository,
                fuenteRepository,
                transitionService,
                eventPublisher,
                alertaRepository
        );
    }

    @Nested
    @DisplayName("Muestra APTA")
    class MuestraApta {

        @Test
        @DisplayName("Retorna EvaluacionResultado APTA cuando arsénico está dentro del límite OMS")
        void retorna_apta_cuando_arsenico_dentro_limite() {
            // Given
            UUID fuenteId  = UUID.randomUUID();
            UUID muestraId = UUID.randomUUID();

            MuestraAgua muestra = muestraEnAnalisis(fuenteId, 5.0);

            when(muestraRepository.findById(muestraId)).thenReturn(Optional.of(muestra));
            when(muestraRepository.save(any())).thenReturn(muestra);

            // When
            EvaluacionResultado resultado = service.execute(
                    new EvaluarMuestraAguaCommand(muestraId, "operador1")
            );

            // Then
            assertThat(resultado.aptaParaConsumo()).isTrue();
            assertThat(resultado.estadoFinal()).isEqualTo(State.APTA);
            assertThat(resultado.alertaGenerada()).isFalse();
            assertThat(resultado.alertaId()).isNull();
        }
    }

    @Nested
    @DisplayName("Muestra NO_APTA")
    class MuestraNoApta {

        @Test
        @DisplayName("Retorna NO_APTA y genera alerta cuando arsénico supera límite OMS")
        void retorna_no_apta_y_genera_alerta() {
            // Given
            UUID fuenteId  = UUID.randomUUID();
            UUID muestraId = UUID.randomUUID();

            MuestraAgua muestra = muestraEnAnalisis(fuenteId, 75.0);
            FuenteAgua fuente   = fuenteExistente(fuenteId);

            when(muestraRepository.findById(muestraId)).thenReturn(Optional.of(muestra));
            when(muestraRepository.save(any())).thenReturn(muestra);
            when(fuenteRepository.findById(fuenteId)).thenReturn(Optional.of(fuente));
            when(fuenteRepository.save(any())).thenReturn(fuente);
            when(alertaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            // When
            EvaluacionResultado resultado = service.execute(
                    new EvaluarMuestraAguaCommand(muestraId, "operador1")
            );

            // Then
            assertThat(resultado.aptaParaConsumo()).isFalse();
            assertThat(resultado.estadoFinal()).isEqualTo(State.NO_APTA);
            assertThat(resultado.alertaGenerada()).isTrue();
            assertThat(resultado.alertaId()).isNotNull();
            verify(alertaRepository).save(any(Alert.class));
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Lanza excepción cuando la muestra no existe")
        void lanza_excepcion_cuando_muestra_no_existe() {
            // Given
            UUID muestraId = UUID.randomUUID();
            when(muestraRepository.findById(muestraId)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> service.execute(
                    new EvaluarMuestraAguaCommand(muestraId, "operador1")
            )).isInstanceOf(ReglaNegocioException.class);
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private MuestraAgua muestraEnAnalisis(UUID fuenteId, double arsenicoUgL) {
        MuestraAgua muestra = MuestraAgua.tomar(fuenteId, "Punto Test");
        muestra.registrarAnalisis(
                new AnalisisQuimico(
                        NivelArsenico.deMicrogramosPorLitro(arsenicoUgL),
                        ParametrosCalidad.de(7.0, 2.0, 20.0),
                        Instant.now()
                ),
                transitionService
        );
        return muestra;
    }

    private FuenteAgua fuenteExistente(UUID id) {
        return new FuenteAgua(
                id,
                "Fuente Test",
                TipoFuente.POZO,
                Ubicacion.de("Mina Clavero", "Córdoba", -31.72, -64.98)
        );
    }
}