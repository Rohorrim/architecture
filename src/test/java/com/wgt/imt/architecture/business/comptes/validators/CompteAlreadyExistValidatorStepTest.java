package com.wgt.imt.architecture.business.comptes.validators;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.exception.ConflictValidationException;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompteAlreadyExistValidatorStep")
class CompteAlreadyExistValidatorStepTest {

    @Mock
    private ClientsBddService clientsBddService;

    private UUID clientUUID;
    private CompteAlreadyExistValidatorStep validator;

    @BeforeEach
    void setUp() {
        clientUUID = UUID.randomUUID();
        validator = new CompteAlreadyExistValidatorStep(clientsBddService, clientUUID);
    }

    @Nested
    @DisplayName("check(Compte)")
    class Check {

        @Test
        @DisplayName("Devrait valider un nouveau compte qui n'existe pas encore")
        void shouldValidateNewAccountThatDoesNotExist() {
            // Given
            Compte newCompte = Compte.builder()
                    .name("Nouveau_Compte")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            Client client = Client.builder()
                    .identifier(clientUUID)
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .comptes(Collections.emptySet())
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(client));

            // When & Then
            assertDoesNotThrow(() -> validator.check(newCompte));
            verify(clientsBddService, times(1)).get(clientUUID);
        }

        @Test
        @DisplayName("Devrait valider un compte avec un nom différent")
        void shouldValidateAccountWithDifferentName() {
            // Given
            Compte existingCompte = Compte.builder()
                    .name("Compte_Existant")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            Compte newCompte = Compte.builder()
                    .name("Nouveau_Compte")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            Client client = Client.builder()
                    .identifier(clientUUID)
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .comptes(new HashSet<>(Collections.singletonList(existingCompte)))
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(client));

            // When & Then
            assertDoesNotThrow(() -> validator.check(newCompte));
        }

        @Test
        @DisplayName("Devrait lever BusinessConflictException si un compte identique existe déjà")
        void shouldThrowBusinessConflictExceptionWhenAccountAlreadyExists() {
            // Given
            Compte existingCompte = Compte.builder()
                    .name("Mon_Compte")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            Compte newCompte = Compte.builder()
                    .name("MON_COMPTE")  // Même nom en majuscules
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            Client client = Client.builder()
                    .identifier(clientUUID)
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .comptes(new HashSet<>(Collections.singletonList(existingCompte)))
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(client));

            // When & Then
            ConflictValidationException exception = assertThrows(ConflictValidationException.class,
                    () -> validator.check(newCompte));

            assertTrue(exception.getMessage().contains("Mon_Compte") ||
                    exception.getMessage().contains("MON_COMPTE"));
            assertTrue(exception.getMessage().contains(clientUUID.toString()));
            verify(clientsBddService, times(1)).get(clientUUID);
        }

        @Test
        @DisplayName("Devrait être insensible à la casse pour le nom du compte")
        void shouldBeCaseInsensitiveForAccountName() {
            // Given
            Compte existingCompte = Compte.builder()
                    .name("livret_a")
                    .type(TypeCompteEnum.LIVRET_A)
                    .build();

            Compte newCompte = Compte.builder()
                    .name("LIVRET_A")
                    .type(TypeCompteEnum.LIVRET_A)
                    .build();

            Client client = Client.builder()
                    .identifier(clientUUID)
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .comptes(new HashSet<>(Collections.singletonList(existingCompte)))
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(client));

            // When & Then
            assertThrows(ConflictValidationException.class, () -> validator.check(newCompte));
        }

        @Test
        @DisplayName("Devrait valider si le type est différent même avec le même nom")
        void shouldValidateIfTypeIsDifferentWithSameName() {
            // Given
            Compte existingCompte = Compte.builder()
                    .name("Mon_Compte")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            Compte newCompte = Compte.builder()
                    .name("Mon_Compte")
                    .type(TypeCompteEnum.LIVRET_A)
                    .build();

            Client client = Client.builder()
                    .identifier(clientUUID)
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .comptes(new HashSet<>(Collections.singletonList(existingCompte)))
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(client));

            // When & Then
            assertDoesNotThrow(() -> validator.check(newCompte));
        }

        @Test
        @DisplayName("Devrait gérer le cas où le client n'existe pas")
        void shouldHandleWhenClientDoesNotExist() {
            // Given
            Compte newCompte = Compte.builder()
                    .name("Nouveau_Compte")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            // When & Then
            assertDoesNotThrow(() -> validator.check(newCompte));
        }

        @Test
        @DisplayName("Devrait gérer le cas où le client n'a pas de comptes")
        void shouldHandleClientWithNoAccounts() {
            // Given
            Compte newCompte = Compte.builder()
                    .name("Premier_Compte")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();

            Client client = Client.builder()
                    .identifier(clientUUID)
                    .lastname("Dupont")
                    .firstname("Jean")
                    .genre(GenreEnum.HOMME)
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(client));

            // When & Then
            assertDoesNotThrow(() -> validator.check(newCompte));
        }
    }
}
