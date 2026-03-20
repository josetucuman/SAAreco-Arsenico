package com.b2b2solutions.application.service;

import com.b2bsolutions.application.ports.out.FuenteAguaRepository;
import com.b2bsolutions.application.response.FuenteAguaResponse;
import com.b2bsolutions.application.service.FuenteAguaQueryService;
import com.b2bsolutions.domain.exception.ReglaNegocioException;
import com.b2bsolutions.domain.font.FuenteAgua;
import com.b2bsolutions.domain.font.Ubicacion;
import com.b2bsolutions.domain.font.enums.EstadoSanitario;
import com.b2bsolutions.domain.font.enums.TipoFuente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FuenteAguaQueryService")
class FuenteAguaQueryServiceTest {

    @Mock FuenteAguaRepository repository;

    FuenteAguaQueryService service;

    @BeforeEach
    void setUp() {
        service = new FuenteAguaQueryService(repository);
    }

    @Test
    @DisplayName("Retorna FuenteAguaResponse cuando la fuente existe")
    void retorna_response_cuando_fuente_existe() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(fuenteExistente(id)));

        // When
        FuenteAguaResponse response = service.findById(id);

        // Then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.nombre()).isEqualTo("Fuente Test");
        assertThat(response.tipo()).isEqualTo(TipoFuente.POZO);
        assertThat(response.localidad()).isEqualTo("Mina Clavero");
        assertThat(response.provincia()).isEqualTo("Córdoba");
        assertThat(response.estadoSanitario()).isEqualTo(EstadoSanitario.DESCONOCIDO);
        assertThat(response.aptaParaConsumo()).isFalse();
        assertThat(response.colorHex()).isEqualTo("#6B7280");
    }

    @Test
    @DisplayName("Lanza excepción cuando la fuente no existe")
    void lanza_excepcion_cuando_fuente_no_existe() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("Retorna lista de responses cuando hay fuentes")
    void retorna_lista_cuando_hay_fuentes() {
        // Given
        when(repository.findAll()).thenReturn(List.of(
                fuenteExistente(UUID.randomUUID()),
                fuenteExistente(UUID.randomUUID())
        ));

        // When
        List<FuenteAguaResponse> responses = service.findAll();

        // Then
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("Retorna lista vacía cuando no hay fuentes")
    void retorna_lista_vacia_cuando_no_hay_fuentes() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<FuenteAguaResponse> responses = service.findAll();

        // Then
        assertThat(responses).isEmpty();
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private FuenteAgua fuenteExistente(UUID id) {
        return new FuenteAgua(
                id, "Fuente Test", TipoFuente.POZO,
                Ubicacion.de("Mina Clavero", "Córdoba", -31.72, -64.98)
        );
    }
}