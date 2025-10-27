package com.wgt.imt.architecture.business.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GenreEnum")
class GenreEnumTest {

    @Nested
    @DisplayName("from(String)")
    class From {

        @Test
        @DisplayName("Devrait retourner HOMME pour 'HOMME'")
        void shouldReturnHommeForHommeString() {
            // When
            Optional<GenreEnum> result = GenreEnum.from("HOMME");

            // Then
            assertTrue(result.isPresent());
            assertEquals(GenreEnum.HOMME, result.get());
        }

        @Test
        @DisplayName("Devrait retourner FEMME pour 'FEMME'")
        void shouldReturnFemmeForFemmeString() {
            // When
            Optional<GenreEnum> result = GenreEnum.from("FEMME");

            // Then
            assertTrue(result.isPresent());
            assertEquals(GenreEnum.FEMME, result.get());
        }

        @Test
        @DisplayName("Devrait retourner NON_COMMUNIQUER pour 'NON_COMMUNIQUER'")
        void shouldReturnNonCommuniquerForNonCommuniquerString() {
            // When
            Optional<GenreEnum> result = GenreEnum.from("NON_COMMUNIQUER");

            // Then
            assertTrue(result.isPresent());
            assertEquals(GenreEnum.NON_COMMUNIQUER, result.get());
        }

        @Test
        @DisplayName("Devrait retourner INCONNU pour 'INCONNU'")
        void shouldReturnInconnuForInconnuString() {
            // When
            Optional<GenreEnum> result = GenreEnum.from("INCONNU");

            // Then
            assertTrue(result.isPresent());
            assertEquals(GenreEnum.INCONNU, result.get());
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty pour une valeur null")
        void shouldReturnEmptyOptionalForNull() {
            // When
            Optional<GenreEnum> result = GenreEnum.from(null);

            // Then
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Devrait lever IllegalArgumentException pour une valeur invalide")
        void shouldThrowExceptionForInvalidValue() {
            // When & Then
            assertEquals(Optional.empty(), GenreEnum.from("INVALIDE"));
        }
    }

    @Nested
    @DisplayName("fromOrDefault(String)")
    class FromOrDefault {

        @Test
        @DisplayName("Devrait retourner HOMME pour 'HOMME'")
        void shouldReturnHommeForHommeString() {
            // When
            GenreEnum result = GenreEnum.fromOrDefault("HOMME");

            // Then
            assertEquals(GenreEnum.HOMME, result);
        }

        @Test
        @DisplayName("Devrait retourner FEMME pour 'FEMME'")
        void shouldReturnFemmeForFemmeString() {
            // When
            GenreEnum result = GenreEnum.fromOrDefault("FEMME");

            // Then
            assertEquals(GenreEnum.FEMME, result);
        }

        @Test
        @DisplayName("Devrait retourner NON_COMMUNIQUER pour 'NON_COMMUNIQUER'")
        void shouldReturnNonCommuniquerForNonCommuniquerString() {
            // When
            GenreEnum result = GenreEnum.fromOrDefault("NON_COMMUNIQUER");

            // Then
            assertEquals(GenreEnum.NON_COMMUNIQUER, result);
        }

        @Test
        @DisplayName("Devrait retourner INCONNU pour une valeur null")
        void shouldReturnInconnuForNull() {
            // When
            GenreEnum result = GenreEnum.fromOrDefault(null);

            // Then
            assertEquals(GenreEnum.INCONNU, result);
        }

        @Test
        @DisplayName("Devrait retourner INCONNU pour une valeur invalide")
        void shouldReturnInconnuForInvalidValue() {
            // When
            GenreEnum result = GenreEnum.fromOrDefault("INVALIDE");

            // Then
            assertEquals(GenreEnum.INCONNU, result);
        }
    }

    @Nested
    @DisplayName("ACCEPTABLE_VALUES")
    class AcceptableValues {

        @Test
        @DisplayName("Devrait contenir les valeurs acceptables")
        void shouldContainAcceptableValues() {
            // Then
            assertNotNull(GenreEnum.ACCEPTABLE_VALUES);
            assertTrue(GenreEnum.ACCEPTABLE_VALUES.contains("HOMME"));
            assertTrue(GenreEnum.ACCEPTABLE_VALUES.contains("FEMME"));
            assertTrue(GenreEnum.ACCEPTABLE_VALUES.contains("NON_COMMUNIQUER"));
            assertFalse(GenreEnum.ACCEPTABLE_VALUES.contains("INCONNU"));
        }
    }
}

