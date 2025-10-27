package com.wgt.imt.architecture.business.clients.validators;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClientGenreValidatorStep")
class ClientGenreValidatorStepTest {

    private final ClientGenreValidatorStep validator = new ClientGenreValidatorStep();

    @Nested
    @DisplayName("check(Client)")
    class Check {

        @Test
        @DisplayName("Devrait valider un client avec un genre valide HOMME")
        void shouldValidateClientWithGenreHomme() {
            // Given
            Client client = Client.builder()
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .build();

            // When & Then
            assertDoesNotThrow(() -> validator.check(client));
        }

        @Test
        @DisplayName("Devrait valider un client avec un genre valide FEMME")
        void shouldValidateClientWithGenreFemme() {
            // Given
            Client client = Client.builder()
                    .lastname("Martin")
                    .firstname("Marie")
                    .genre(GenreEnum.FEMME)
                    .build();

            // When & Then
            assertDoesNotThrow(() -> validator.check(client));
        }

        @Test
        @DisplayName("Devrait valider un client avec un genre NON_COMMUNIQUER")
        void shouldValidateClientWithGenreNonCommuniquer() {
            // Given
            Client client = Client.builder()
                    .lastname("Durand")
                    .firstname("Alex")
                    .genre(GenreEnum.NON_COMMUNIQUER)
                    .build();

            // When & Then
            assertDoesNotThrow(() -> validator.check(client));
        }

        @Test
        @DisplayName("Devrait lever BusinessValidationException si le genre est INCONNU")
        void shouldThrowBusinessValidationExceptionWhenGenreIsInconnu() {
            // Given
            Client client = Client.builder()
                    .lastname("Test")
                    .firstname("Test")
                    .genre(GenreEnum.INCONNU)
                    .build();

            // When & Then
            ConstraintValidationException exception = assertThrows(ConstraintValidationException.class,
                    () -> validator.check(client));

            assertTrue(exception.getMessage().contains("genre"));
            assertTrue(exception.getMessage().contains(GenreEnum.ACCEPTABLE_VALUES));
        }
    }
}
