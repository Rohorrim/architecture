package com.wgt.imt.architecture.business.clients.validators;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.exception.ConflictValidationException;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientAlreadyExistValidatorStep")
class ClientAlreadyExistValidatorStepTest {

    @Mock
    private ClientsBddService clientsBddService;

    private ClientAlreadyExistValidatorStep validator;

    @BeforeEach
    void setUp() {
        validator = new ClientAlreadyExistValidatorStep(clientsBddService);
    }

    @Nested
    @DisplayName("check(Client)")
    class Check {

        @Test
        @DisplayName("Devrait valider un nouveau client qui n'existe pas encore")
        void shouldValidateNewClientThatDoesNotExist() {
            // Given
            Client newClient = Client.builder()
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .build();

            when(clientsBddService.getAll()).thenReturn(Collections.emptySet());

            // When & Then
            assertDoesNotThrow(() -> validator.check(newClient));
            verify(clientsBddService, times(1)).getAll();
        }

        @Test
        @DisplayName("Devrait valider un client avec un nom différent")
        void shouldValidateClientWithDifferentName() {
            // Given
            Client existingClient = Client.builder()
                    .lastname("Martin")
                    .firstname("Marie")
                    .genre(GenreEnum.FEMME)
                    .build();

            Client newClient = Client.builder()
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .build();

            when(clientsBddService.getAll()).thenReturn(Collections.singletonList(existingClient));

            // When & Then
            assertDoesNotThrow(() -> validator.check(newClient));
        }

        @Test
        @DisplayName("Devrait lever ConflictValidationException si un client identique existe déjà")
        void shouldThrowBusinessConflictExceptionWhenClientAlreadyExists() {
            // Given
            Client existingClient = Client.builder()
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .build();

            Client newClient = Client.builder()
                    .lastname("DUPONT")  // Même nom en majuscules
                    .firstname("JEAN")   // Même prénom en majuscules
                    .genre(GenreEnum.HOMME)
                    .build();

            when(clientsBddService.getAll()).thenReturn(Collections.singletonList(existingClient));

            // When & Then
            ConflictValidationException exception = assertThrows(ConflictValidationException.class,
                    () -> validator.check(newClient));

            assertTrue(exception.getMessage().contains("DUPONT"));
            assertTrue(exception.getMessage().contains("JEAN"));
            verify(clientsBddService, times(1)).getAll();
        }

        @Test
        @DisplayName("Devrait être insensible à la casse pour le nom et prénom")
        void shouldBeCaseInsensitiveForNameAndFirstname() {
            // Given
            Client existingClient = Client.builder()
                    .lastname("martin")
                    .firstname("marie")
                    .genre(GenreEnum.FEMME)
                    .build();

            Client newClient = Client.builder()
                    .lastname("MARTIN")
                    .firstname("MARIE")
                    .genre(GenreEnum.FEMME)
                    .build();

            when(clientsBddService.getAll()).thenReturn(Collections.singletonList(existingClient));

            // When & Then
            assertThrows(ConflictValidationException.class, () -> validator.check(newClient));
        }

        @Test
        @DisplayName("Devrait valider si le genre est différent même avec le même nom")
        void shouldValidateIfGenreIsDifferentWithSameName() {
            // Given
            Client existingClient = Client.builder()
                    .lastname("Dupont")
                    .firstname("Alex")
                    .genre(GenreEnum.HOMME)
                    .build();

            Client newClient = Client.builder()
                    .lastname("Dupont")
                    .firstname("Alex")
                    .genre(GenreEnum.FEMME)
                    .build();

            when(clientsBddService.getAll()).thenReturn(Collections.singletonList(existingClient));

            // When & Then
            assertDoesNotThrow(() -> validator.check(newClient));
        }

        @Test
        @DisplayName("Devrait gérer le cas où getAll retourne null")
        void shouldHandleNullFromGetAll() {
            // Given
            Client newClient = Client.builder()
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .build();

            when(clientsBddService.getAll()).thenReturn(null);

            // When & Then
            assertDoesNotThrow(() -> validator.check(newClient));
        }
    }
}