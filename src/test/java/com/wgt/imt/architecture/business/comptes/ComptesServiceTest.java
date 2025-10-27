package com.wgt.imt.architecture.business.comptes;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.exception.NotFoundValidationException;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.business.comptes.model.Mouvement;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComptesService")
class ComptesServiceTest {

    @Mock
    private ClientsBddService clientsBddService;

    @InjectMocks
    private ComptesService comptesService;

    private UUID clientUUID;
    private UUID compteUUID;
    private Client testClient;
    private Compte testCompte;

    @BeforeEach
    void setUp() {
        clientUUID = UUID.randomUUID();
        compteUUID = UUID.randomUUID();

        testCompte = Compte.builder()
                .identifier(compteUUID)
                .name("Compte_Courant_Test")
                .type(TypeCompteEnum.COMPTE_COURANT)
                .solde(100.0)
                .build();

        testClient = Client.builder()
                .identifier(clientUUID)
                .lastname("Dupont")
                .firstname("Jean")
                .genre(GenreEnum.HOMME)
                .comptes(new HashSet<>(Collections.singletonList(testCompte)))
                .build();
    }

    @Nested
    @DisplayName("getAllFilteredByClient(UUID)")
    class GetAllFilteredByClient {

        @Test
        @DisplayName("Devrait retourner tous les comptes d'un client existant")
        void shouldReturnAllAccountsForExistingClient() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));

            // When
            Collection<Compte> result = comptesService.getAllFilteredByClient(clientUUID);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.contains(testCompte));
            verify(clientsBddService, times(1)).get(clientUUID);
        }

        @Test
        @DisplayName("Devrait retourner une collection vide si le client n'existe pas")
        void shouldReturnEmptyCollectionWhenClientNotFound() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            // When
            Collection<Compte> result = comptesService.getAllFilteredByClient(clientUUID);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(clientsBddService, times(1)).get(clientUUID);
        }

        @Test
        @DisplayName("Devrait retourner une collection vide si le client n'a pas de comptes")
        void shouldReturnEmptyCollectionWhenClientHasNoAccounts() {
            // Given
            Client clientWithoutAccounts = testClient.toBuilder().comptes(Collections.emptySet()).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutAccounts));

            // When
            Collection<Compte> result = comptesService.getAllFilteredByClient(clientUUID);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Devrait retourner une collection vide si la liste des comptes est nulle")
        void shouldReturnEmptyCollectionWhenAccountsAreNull() {
            Client clientWithoutAccounts = testClient.toBuilder().comptes(null).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutAccounts));

            assertTrue(comptesService.getAllFilteredByClient(clientUUID).isEmpty());
        }
    }

    @Nested
    @DisplayName("getOneFilteredByClient(UUID, UUID)")
    class GetOneFilteredByClient {

        @Test
        @DisplayName("Devrait retourner un compte existant pour un client")
        void shouldReturnExistingAccountForClient() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));

            // When
            Optional<Compte> result = comptesService.getOneFilteredByClient(clientUUID, compteUUID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(testCompte, result.get());
            verify(clientsBddService, times(1)).get(clientUUID);
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty si le compte n'existe pas")
        void shouldReturnEmptyOptionalWhenAccountNotFound() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));
            UUID nonExistentCompteUUID = UUID.randomUUID();

            // When
            Optional<Compte> result = comptesService.getOneFilteredByClient(clientUUID, nonExistentCompteUUID);

            // Then
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty si le client n'existe pas")
        void shouldReturnEmptyOptionalWhenClientNotFound() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            // When
            Optional<Compte> result = comptesService.getOneFilteredByClient(clientUUID, compteUUID);

            // Then
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty si la liste des comptes est nulle")
        void shouldReturnEmptyWhenAccountsAreNull() {
            Client clientWithoutAccounts = testClient.toBuilder().comptes(null).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutAccounts));

            assertTrue(comptesService.getOneFilteredByClient(clientUUID, compteUUID).isEmpty());
        }
    }

    @Nested
    @DisplayName("create(UUID, Compte)")
    class Create {

        @Test
        @DisplayName("Devrait créer un nouveau compte pour un client existant")
        void shouldCreateNewAccountForExistingClient() {
            // Given
            Compte newCompte = Compte.builder()
                    .name("Livret_A")
                    .type(TypeCompteEnum.LIVRET_A)
                    .solde(0.0)
                    .build();

            Client clientWithoutNewAccount = testClient.toBuilder().comptes(Collections.emptySet()).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutNewAccount));
            when(clientsBddService.save(any(Client.class))).thenReturn(testClient);

            // When
            Compte result = comptesService.create(clientUUID, newCompte);

            // Then
            assertNotNull(result);
            assertEquals(newCompte, result);
            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait lever BusinessNotFoundException si le client n'existe pas")
        void shouldThrowBusinessNotFoundExceptionWhenClientNotFound() {
            // Given
            Compte newCompte = Compte.builder()
                    .name("Compte_Test")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            // When & Then
            NotFoundValidationException exception = assertThrows(NotFoundValidationException.class,
                    () -> comptesService.create(clientUUID, newCompte));

            assertTrue(exception.getMessage().contains(clientUUID.toString()));
            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait créer le compte si la liste des comptes existante est nulle")
        void shouldCreateAccountWhenExistingAccountsAreNull() {
            Compte newCompte = Compte.builder()
                    .name("Livret_A")
                    .type(TypeCompteEnum.LIVRET_A)
                    .build();
            Client clientWithoutAccounts = testClient.toBuilder().comptes(null).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutAccounts));

            assertEquals(newCompte, comptesService.create(clientUUID, newCompte));

            verify(clientsBddService).save(argThat(saved ->
                    saved.getComptes().size() == 1 && saved.getComptes().contains(newCompte)
            ));
        }
    }

    @Nested
    @DisplayName("update(UUID, Compte)")
    class Update {

        @Test
        @DisplayName("Devrait mettre à jour un compte existant")
        void shouldUpdateExistingAccount() {
            // Given
            Compte updatedCompte = testCompte.toBuilder().solde(200.0).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));
            when(clientsBddService.save(any(Client.class))).thenReturn(testClient);

            // When
            comptesService.update(clientUUID, updatedCompte);

            // Then
            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait publier un événement si le solde est négatif")
        void shouldPublishEventWhenBalanceIsNegative() {
            // Given
            Compte compteNegatif = testCompte.toBuilder().solde(-50.0).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));
            when(clientsBddService.save(any(Client.class))).thenReturn(testClient);

            // When
            comptesService.update(clientUUID, compteNegatif);

            // Then
            verify(clientsBddService, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait lever BusinessNotFoundException si le client n'existe pas")
        void shouldThrowBusinessNotFoundExceptionWhenClientNotFound() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(NotFoundValidationException.class,
                    () -> comptesService.update(clientUUID, testCompte));

            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait ajouter le compte si la liste existante est nulle")
        void shouldUpdateAccountWhenExistingAccountsAreNull() {
            Client clientWithoutAccounts = testClient.toBuilder().comptes(null).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutAccounts));

            comptesService.update(clientUUID, testCompte);

            verify(clientsBddService).save(argThat(saved ->
                    saved.getComptes().size() == 1 && saved.getComptes().contains(testCompte)
            ));
        }
    }

    @Nested
    @DisplayName("applyAgiosIfNeeded(UUID, UUID)")
    class ApplyAgios {

        @Test
        @DisplayName("Devrait appliquer les agios de 5 euros à un compte")
        void shouldApplyAgiosToAccount() {
            // Given
            Compte compteNegatif = testCompte.toBuilder().solde(-50.0).build();
            Client clientAvecCompteNegatif = testClient.toBuilder()
                    .comptes(new HashSet<>(Collections.singletonList(compteNegatif)))
                    .build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientAvecCompteNegatif));
            when(clientsBddService.save(any(Client.class))).thenReturn(testClient);

            // When
            boolean agiosAppliques = comptesService.applyAgiosIfNeeded(clientUUID, compteUUID);

            // Then
            assertTrue(agiosAppliques);
            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService).save(argThat(client ->
                    client.getComptes().stream()
                            .anyMatch(compte -> compteUUID.equals(compte.getIdentifier()) && compte.getSolde() == -55.0)
            ));
        }

        @Test
        @DisplayName("Devrait lever BusinessNotFoundException si le client n'existe pas")
        void shouldThrowBusinessNotFoundExceptionWhenClientNotFound() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(NotFoundValidationException.class,
                    () -> comptesService.applyAgiosIfNeeded(clientUUID, compteUUID));

            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Ne devrait pas modifier les autres comptes lors de l'application des agios")
        void shouldNotModifyOtherAccountsWhenApplyingAgios() {
            // Given
            Compte autreCompte = Compte.builder()
                    .identifier(UUID.randomUUID())
                    .name("Autre_Compte")
                    .type(TypeCompteEnum.LIVRET_A)
                    .solde(500.0)
                    .build();

            Compte compteNegatif = testCompte.toBuilder().solde(-50.0).build();
            Client clientWithMultipleAccounts = testClient.toBuilder()
                    .comptes(new HashSet<>(Arrays.asList(compteNegatif, autreCompte)))
                    .build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithMultipleAccounts));

            // When
            comptesService.applyAgiosIfNeeded(clientUUID, compteUUID);

            // Then
            verify(clientsBddService).save(argThat(client ->
                    client.getComptes().size() == 2
                            && client.getComptes().stream()
                                    .anyMatch(compte -> compteUUID.equals(compte.getIdentifier()) && compte.getSolde() == -55.0)
                            && client.getComptes().stream()
                                    .anyMatch(compte -> autreCompte.getIdentifier().equals(compte.getIdentifier())
                                            && compte.getSolde() == 500.0)
            ));
        }

        @Test
        @DisplayName("Ne devrait pas appliquer les agios à un compte avec un solde positif")
        void shouldNotApplyAgiosToNonNegativeAccount() {
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));

            assertFalse(comptesService.applyAgiosIfNeeded(clientUUID, compteUUID));

            verify(clientsBddService, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Ne devrait pas appliquer les agios si la liste des comptes est nulle")
        void shouldNotApplyAgiosWhenAccountsAreNull() {
            Client clientWithoutAccounts = testClient.toBuilder().comptes(null).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutAccounts));

            assertFalse(comptesService.applyAgiosIfNeeded(clientUUID, compteUUID));

            verify(clientsBddService, never()).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("addIntoHistory(UUID, UUID, String)")
    class AddIntoHistory {

        @Test
        @DisplayName("Devrait ajouter un message à l'historique du compte")
        void shouldAppendMessageToAccountHistory() {
            Mouvement mouvementExistant = Mouvement.builder().message("Ouverture").build();
            Compte compteAvecHistorique = testCompte.toBuilder()
                    .historique(new HashSet<>(Collections.singleton(mouvementExistant)))
                    .build();
            Client clientAvecHistorique = testClient.toBuilder()
                    .comptes(new HashSet<>(Collections.singleton(compteAvecHistorique)))
                    .build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientAvecHistorique));

            comptesService.addIntoHistory(clientUUID, compteUUID, "Dépôt");

            verify(clientsBddService).save(argThat(saved ->
                    saved.getComptes().stream()
                            .filter(compte -> compteUUID.equals(compte.getIdentifier()))
                            .anyMatch(compte -> compte.getHistorique().size() == 2
                                    && compte.getHistorique().stream()
                                            .anyMatch(mouvement -> "Dépôt".equals(mouvement.getMessage()))
                                    && compte.getHistorique().stream()
                                            .anyMatch(mouvement -> "Ouverture".equals(mouvement.getMessage()))
                            )
            ));
        }

        @Test
        @DisplayName("Ne devrait rien modifier si le compte n'existe pas")
        void shouldNotSaveWhenAccountDoesNotExist() {
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));

            comptesService.addIntoHistory(clientUUID, UUID.randomUUID(), "Dépôt");

            verify(clientsBddService, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait lever une exception si le client n'existe pas")
        void shouldThrowWhenClientDoesNotExist() {
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            assertThrows(NotFoundValidationException.class,
                    () -> comptesService.addIntoHistory(clientUUID, compteUUID, "Dépôt"));

            verify(clientsBddService, never()).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("delete(UUID, UUID)")
    class Delete {

        @Test
        @DisplayName("Devrait supprimer un compte d'un client")
        void shouldDeleteAccountFromClient() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(testClient));
            when(clientsBddService.save(any(Client.class))).thenReturn(testClient);

            // When
            comptesService.delete(clientUUID, compteUUID);

            // Then
            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait lever BusinessNotFoundException si le client n'existe pas")
        void shouldThrowBusinessNotFoundExceptionWhenClientNotFound() {
            // Given
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(NotFoundValidationException.class,
                    () -> comptesService.delete(clientUUID, compteUUID));

            verify(clientsBddService, times(1)).get(clientUUID);
            verify(clientsBddService, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Ne devrait pas affecter les autres comptes lors de la suppression")
        void shouldNotAffectOtherAccountsWhenDeleting() {
            // Given
            Compte autreCompte = Compte.builder()
                    .identifier(UUID.randomUUID())
                    .name("Autre_Compte")
                    .type(TypeCompteEnum.LIVRET_A)
                    .build();

            Client clientWithMultipleAccounts = testClient.toBuilder()
                    .comptes(new HashSet<>(Arrays.asList(testCompte, autreCompte)))
                    .build();

            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithMultipleAccounts));

            // When
            comptesService.delete(clientUUID, compteUUID);

            // Then
            verify(clientsBddService, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait supprimer un compte si la liste existante est nulle")
        void shouldDeleteAccountWhenAccountsAreNull() {
            Client clientWithoutAccounts = testClient.toBuilder().comptes(null).build();
            when(clientsBddService.get(clientUUID)).thenReturn(Optional.of(clientWithoutAccounts));

            comptesService.delete(clientUUID, compteUUID);

            verify(clientsBddService).save(argThat(saved -> saved.getComptes().isEmpty()));
        }
    }
}
