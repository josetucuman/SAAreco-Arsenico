package com.b2bsolutions.domain.shared;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

<<<<<<< HEAD

=======
import com.b2bsolutions.domain.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
>>>>>>> b36d486976ff02c56e0a1e16c683981c6a998a3b

@DisplayName("ConcentracionPPB")
class ConcentracionPPBTest {

    @Nested
    @DisplayName("Creación desde µg/L")
    class DesdeMicrogramos {

        @Test
        @DisplayName("Crea concentración válida desde µg/L")
        void crea_concentracion_valida_desde_ugL() {
            // Given
            double valor = 15.0;

            // When
            ConcentracionPPB concentracion = ConcentracionPPB.deMicrogramosPorLitro(valor);

            // Then
            assertThat(concentracion.enMicrogramosPorLitro()).isEqualTo(15.0);
        }

        @Test
        @DisplayName("Convierte correctamente de mg/L a µg/L")
        void convierte_mgL_a_ugL() {
            // Given
            double valorEnMgL = 0.015;

            // When
            ConcentracionPPB concentracion = ConcentracionPPB.deMiligramosPorLitro(valorEnMgL);

            // Then
            assertThat(concentracion.enMicrogramosPorLitro()).isEqualTo(15.0);
            assertThat(concentracion.enMiligramosPorLitro()).isEqualTo(0.015);
        }

        @Test
        @DisplayName("Acepta valor cero")
        void acepta_valor_cero() {
            // When / Then
            assertThatNoException()
                    .isThrownBy(() -> ConcentracionPPB.deMicrogramosPorLitro(0.0));
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Rechaza concentración negativa")
        void rechaza_concentracion_negativa() {
            // Given
            double valorNegativo = -1.0;

            // When / Then
            assertThatThrownBy(() -> ConcentracionPPB.deMicrogramosPorLitro(valorNegativo))
                    .isInstanceOf(DomainViolationException.class)
                    .hasMessageContaining("negativa");
        }

        @Test
        @DisplayName("Rechaza concentración que supera límite físico")
        void rechaza_concentracion_mayor_a_limite_fisico() {
            // Given
            double valorExcesivo = 6_000.0;

            // When / Then
            assertThatThrownBy(() -> ConcentracionPPB.deMicrogramosPorLitro(valorExcesivo))
                    .isInstanceOf(DomainViolationException.class)
                    .hasMessageContaining("límite físico");
        }
    }

    @Nested
    @DisplayName("Comparaciones")
    class Comparaciones {

        @Test
        @DisplayName("Detecta cuando supera un límite dado")
        void detecta_cuando_supera_limite() {
            // Given
            ConcentracionPPB concentracion = ConcentracionPPB.deMicrogramosPorLitro(15.0);
            ConcentracionPPB limite        = ConcentracionPPB.deMicrogramosPorLitro(10.0);

            // When / Then
            assertThat(concentracion.superaLimite(limite)).isTrue();
        }

        @Test
        @DisplayName("No supera el límite cuando está por debajo")
        void no_supera_limite_cuando_esta_por_debajo() {
            // Given
            ConcentracionPPB concentracion = ConcentracionPPB.deMicrogramosPorLitro(8.0);
            ConcentracionPPB limite        = ConcentracionPPB.deMicrogramosPorLitro(10.0);

            // When / Then
            assertThat(concentracion.superaLimite(limite)).isFalse();
        }

        @Test
        @DisplayName("Calcula correctamente la reducción porcentual")
        void calcula_reduccion_porcentual() {
            // Given
            ConcentracionPPB concentracion = ConcentracionPPB.deMicrogramosPorLitro(20.0);

            // When
            ConcentracionPPB reducida = concentracion.reducirEn(50.0);

            // Then
            assertThat(reducida.enMicrogramosPorLitro()).isEqualTo(10.0);
        }

        @Test
        @DisplayName("Rechaza porcentaje de reducción mayor a 100")
        void rechaza_porcentaje_reduccion_invalido() {
            // Given
            ConcentracionPPB concentracion = ConcentracionPPB.deMicrogramosPorLitro(20.0);

            // When / Then
            assertThatThrownBy(() -> concentracion.reducirEn(110.0))
                    .isInstanceOf(DomainViolationException.class);
        }
    }

    @Nested
    @DisplayName("Igualdad")
    class Igualdad {

        @Test
        @DisplayName("Dos concentraciones con el mismo valor son iguales")
        void dos_concentraciones_iguales() {
            // Given
            ConcentracionPPB a = ConcentracionPPB.deMicrogramosPorLitro(10.0);
            ConcentracionPPB b = ConcentracionPPB.deMicrogramosPorLitro(10.0);

            // When / Then
            assertThat(a).isEqualTo(b);
            assertThat(a.hashCode()).isEqualTo(b.hashCode());
        }
    }
}