package com.b2bsolutions.domain.shared.model;

import com.b2bsolutions.domain.font.enums.Clasificacion;
import com.b2bsolutions.domain.model.NivelArsenico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NivelArsenico")
class NivelArsenicoTest {

    @Nested
    @DisplayName("Clasificación")
    class ClasificacionTest {  // ← renombrado para evitar conflicto con el enum importado

        @Test
        @DisplayName("Clasifica como ACEPTABLE cuando está dentro del límite OMS (≤10 µg/L)")
        void clasifica_aceptable_dentro_limite_oms() {
            // Given
            NivelArsenico nivel = NivelArsenico.deMicrogramosPorLitro(8.0);

            // When / Then
            assertThat(nivel.getClasificacion()).isEqualTo(Clasificacion.ACEPTABLE);
            assertThat(nivel.esAceptable()).isTrue();
            assertThat(nivel.superaLimiteOMS()).isFalse();
        }

        @Test
        @DisplayName("Clasifica como ELEVADO cuando supera OMS pero no llega a 50 µg/L")
        void clasifica_elevado_entre_10_y_50() {
            // Given
            NivelArsenico nivel = NivelArsenico.deMicrogramosPorLitro(30.0);

            // When / Then
            assertThat(nivel.getClasificacion()).isEqualTo(Clasificacion.ELEVADO);
            assertThat(nivel.requiereAtencion()).isTrue();
            assertThat(nivel.esCritico()).isFalse();
        }

        @Test
        @DisplayName("Clasifica como CRITICO cuando supera 50 µg/L")
        void clasifica_critico_sobre_50() {
            // Given
            NivelArsenico nivel = NivelArsenico.deMicrogramosPorLitro(75.0);

            // When / Then
            assertThat(nivel.getClasificacion()).isEqualTo(Clasificacion.CRITICO);
            assertThat(nivel.esCritico()).isTrue();
            assertThat(nivel.requiereAtencion()).isTrue();
        }

        @Test
        @DisplayName("Clasifica exactamente en el límite OMS (10 µg/L) como ACEPTABLE")
        void clasifica_exactamente_en_limite_oms() {
            // Given
            NivelArsenico nivel = NivelArsenico.deMicrogramosPorLitro(10.0);

            // When / Then
            assertThat(nivel.esAceptable()).isTrue();
        }
    }

    @Nested
    @DisplayName("Porcentaje de reducción necesario")
    class PorcentajeReduccion {

        @Test
        @DisplayName("Retorna 0 cuando no supera el límite OMS")
        void retorna_cero_cuando_no_supera_limite() {
            // Given
            NivelArsenico nivel = NivelArsenico.deMicrogramosPorLitro(8.0);

            // When / Then
            assertThat(nivel.porcentajeReduccionNecesario()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Calcula correctamente el porcentaje cuando supera el límite")
        void calcula_porcentaje_reduccion_correcto() {
            // Given — 20 µg/L necesita reducir 50% para llegar a 10 µg/L
            NivelArsenico nivel = NivelArsenico.deMicrogramosPorLitro(20.0);

            // When
            double porcentaje = nivel.porcentajeReduccionNecesario();

            // Then
            assertThat(porcentaje).isEqualTo(50.0);
        }
    }

    @Nested
    @DisplayName("Efectividad del filtrado")
    class EfectividadFiltrado {

        @Test
        @DisplayName("Filtrado es efectivo si resultado está dentro del límite OMS")
        void filtrado_efectivo_cuando_resultado_es_aceptable() {
            // Given
            NivelArsenico nivelInicial      = NivelArsenico.deMicrogramosPorLitro(45.0);
            NivelArsenico nivelPostFiltrado = NivelArsenico.deMicrogramosPorLitro(7.0);

            // When / Then
            assertThat(nivelInicial.filtradoEsEfectivo(nivelPostFiltrado)).isTrue();
        }

        @Test
        @DisplayName("Filtrado no es efectivo si resultado sigue sobre el límite OMS")
        void filtrado_no_efectivo_cuando_sigue_sobre_limite() {
            // Given
            NivelArsenico nivelInicial      = NivelArsenico.deMicrogramosPorLitro(45.0);
            NivelArsenico nivelPostFiltrado = NivelArsenico.deMicrogramosPorLitro(12.0);

            // When / Then
            assertThat(nivelInicial.filtradoEsEfectivo(nivelPostFiltrado)).isFalse();
        }
    }
}