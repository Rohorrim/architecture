package com.wgt.imt.architecture.business.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TypeCompteEnum")
class TypeCompteEnumTest {

    @Nested
    @DisplayName("from(String)")
    class From {

        @Test
        @DisplayName("Devrait retourner COMPTE_COURANT pour 'COMPTE_COURANT'")
        void shouldReturnCompteCourantForCompteCourantString() {
            // When
            Optional<TypeCompteEnum> result = TypeCompteEnum.from("COMPTE_COURANT");

            // Then
            assertTrue(result.isPresent());
            assertEquals(TypeCompteEnum.COMPTE_COURANT, result.get());
        }

        @Test
        @DisplayName("Devrait retourner LIVRET_A pour 'LIVRET_A'")
        void shouldReturnLivretAForLivretAString() {
            // When
            Optional<TypeCompteEnum> result = TypeCompteEnum.from("LIVRET_A");

            // Then
            assertTrue(result.isPresent());
            assertEquals(TypeCompteEnum.LIVRET_A, result.get());
        }

        @Test
        @DisplayName("Devrait retourner LDDS pour 'LDDS'")
        void shouldReturnLDDSForLDDSString() {
            // When
            Optional<TypeCompteEnum> result = TypeCompteEnum.from("LDDS");

            // Then
            assertTrue(result.isPresent());
            assertEquals(TypeCompteEnum.LDDS, result.get());
        }

        @Test
        @DisplayName("Devrait retourner INCONNU pour 'INCONNU'")
        void shouldReturnInconnuForInconnuString() {
            // When
            Optional<TypeCompteEnum> result = TypeCompteEnum.from("INCONNU");

            // Then
            assertTrue(result.isPresent());
            assertEquals(TypeCompteEnum.INCONNU, result.get());
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty pour une valeur null")
        void shouldReturnEmptyOptionalForNull() {
            // When
            Optional<TypeCompteEnum> result = TypeCompteEnum.from(null);

            // Then
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Devrait lever IllegalArgumentException pour une valeur invalide")
        void shouldReturnOptionalEmpty() {
            // When & Then
            assertEquals(Optional.empty(), TypeCompteEnum.from("INVALIDE"));
        }
    }

    @Nested
    @DisplayName("fromOrDefault(String)")
    class FromOrDefault {

        @Test
        @DisplayName("Devrait retourner COMPTE_COURANT pour 'COMPTE_COURANT'")
        void shouldReturnCompteCourantForCompteCourantString() {
            // When
            TypeCompteEnum result = TypeCompteEnum.fromOrDefault("COMPTE_COURANT");

            // Then
            assertEquals(TypeCompteEnum.COMPTE_COURANT, result);
        }

        @Test
        @DisplayName("Devrait retourner LIVRET_A pour 'LIVRET_A'")
        void shouldReturnLivretAForLivretAString() {
            // When
            TypeCompteEnum result = TypeCompteEnum.fromOrDefault("LIVRET_A");

            // Then
            assertEquals(TypeCompteEnum.LIVRET_A, result);
        }

        @Test
        @DisplayName("Devrait retourner LDDS pour 'LDDS'")
        void shouldReturnLDDSForLDDSString() {
            // When
            TypeCompteEnum result = TypeCompteEnum.fromOrDefault("LDDS");

            // Then
            assertEquals(TypeCompteEnum.LDDS, result);
        }

        @Test
        @DisplayName("Devrait retourner INCONNU pour une valeur null")
        void shouldReturnInconnuForNull() {
            // When
            TypeCompteEnum result = TypeCompteEnum.fromOrDefault(null);

            // Then
            assertEquals(TypeCompteEnum.INCONNU, result);
        }

        @Test
        @DisplayName("Devrait retourner INCONNU pour une valeur invalide")
        void shouldReturnInconnuForInvalidValue() {
            // When
            TypeCompteEnum result = TypeCompteEnum.fromOrDefault("INVALIDE");

            // Then
            assertEquals(TypeCompteEnum.INCONNU, result);
        }
    }

    @Nested
    @DisplayName("ACCEPTABLE_VALUES")
    class AcceptableValues {

        @Test
        @DisplayName("Devrait contenir les valeurs acceptables")
        void shouldContainAcceptableValues() {
            // Then
            assertNotNull(TypeCompteEnum.ACCEPTABLE_VALUES);
            assertTrue(TypeCompteEnum.ACCEPTABLE_VALUES.contains("COMPTE_COURANT"));
            assertTrue(TypeCompteEnum.ACCEPTABLE_VALUES.contains("LIVRET_A"));
            assertTrue(TypeCompteEnum.ACCEPTABLE_VALUES.contains("LDDS"));
            assertFalse(TypeCompteEnum.ACCEPTABLE_VALUES.contains("INCONNU"));
        }
    }
}

