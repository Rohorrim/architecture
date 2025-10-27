package com.wgt.imt.architecture.business.clients;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
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
@DisplayName("ClientsService")
class ClientsServiceTest {

    @Mock
    private ClientsBddService clientsBddService;

    @InjectMocks
    private ClientsService clientsService;

    private Client testClient;
    private UUID testUUID;

    @BeforeEach
    void setUp() {
        testUUID = UUID.randomUUID();
        testClient = Client.builder()
                .identifier(testUUID)
                .lastname("Dupont")
                .firstname("Jean")
                .genre(GenreEnum.HOMME)
                .build();
    }

    @Nested
    @DisplayName("getAll()")
    class GetAll {

        @Test
        @DisplayName("Devrait retourner tous les clients")
        void shouldReturnAllClients() {
            // Given
            Collection<Client> clients = Arrays.asList(testClient);
            when(clientsBddService.getAll()).thenReturn(clients);

            // When
            Collection<Client> result = clientsService.getAll();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(clientsBddService, times(1)).getAll();
        }

        @Test
        @DisplayName("Devrait retourner une collection vide si le service retourne null")
        void shouldReturnEmptyCollectionWhenServiceReturnsNull() {
            // Given
            when(clientsBddService.getAll()).thenReturn(null);

            // When
            Collection<Client> result = clientsService.getAll();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(clientsBddService, times(1)).getAll();
        }

        @Test
        @DisplayName("Devrait retourner une collection vide si le service retourne une collection vide")
        void shouldReturnEmptyCollectionWhenServiceReturnsEmpty() {
            // Given
            when(clientsBddService.getAll()).thenReturn(Collections.emptySet());

            // When
            Collection<Client> result = clientsService.getAll();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(clientsBddService, times(1)).getAll();
        }
    }

    @Nested
    @DisplayName("getOne(UUID)")
    class GetOne {

        @Test
        @DisplayName("Devrait retourner un client existant")
        void shouldReturnExistingClient() {
            // Given
            when(clientsBddService.get(testUUID)).thenReturn(Optional.of(testClient));

            // When
            Optional<Client> result = clientsService.getOne(testUUID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(testClient, result.get());
            verify(clientsBddService, times(1)).get(testUUID);
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty si le client n'existe pas")
        void shouldReturnEmptyOptionalWhenClientNotFound() {
            // Given
            when(clientsBddService.get(testUUID)).thenReturn(Optional.empty());

            // When
            Optional<Client> result = clientsService.getOne(testUUID);

            // Then
            assertFalse(result.isPresent());
            verify(clientsBddService, times(1)).get(testUUID);
        }
    }

    @Nested
    @DisplayName("create(Client)")
    class Create {

        @Test
        @DisplayName("Devrait créer un nouveau client")
        void shouldCreateNewClient() {
            // Given
            when(clientsBddService.save(testClient)).thenReturn(testClient);

            // When
            Client result = clientsService.create(testClient);

            // Then
            assertNotNull(result);
            assertEquals(testClient, result);
            verify(clientsBddService, times(1)).save(testClient);
        }

        @Test
        @DisplayName("Devrait créer un client avec tous les attributs")
        void shouldCreateClientWithAllAttributes() {
            // Given
            Client newClient = Client.builder()
                    .lastname("Martin")
                    .firstname("Marie")
                    .genre(GenreEnum.FEMME)
                    .build();
            when(clientsBddService.save(newClient)).thenReturn(newClient);

            // When
            Client result = clientsService.create(newClient);

            // Then
            assertNotNull(result);
            assertEquals("Martin", result.getLastname());
            assertEquals("Marie", result.getFirstname());
            assertEquals(GenreEnum.FEMME, result.getGenre());
            verify(clientsBddService, times(1)).save(newClient);
        }
    }

    @Nested
    @DisplayName("update(Client)")
    class Update {

        @Test
        @DisplayName("Devrait mettre à jour un client existant")
        void shouldUpdateExistingClient() {
            // Given
            Client updatedClient = testClient.toBuilder()
                    .lastname("Durand")
                    .build();

            // When
            clientsService.update(updatedClient);

            // Then
            verify(clientsBddService, times(1)).save(updatedClient);
        }

        @Test
        @DisplayName("Devrait appeler le service de sauvegarde avec le client modifié")
        void shouldCallSaveServiceWithUpdatedClient() {
            // Given
            when(clientsBddService.save(any(Client.class))).thenReturn(testClient);

            // When
            clientsService.update(testClient);

            // Then
            verify(clientsBddService, times(1)).save(testClient);
        }
    }

    @Nested
    @DisplayName("delete(UUID)")
    class Delete {

        @Test
        @DisplayName("Devrait supprimer un client par son identifiant")
        void shouldDeleteClientByIdentifier() {
            // Given
            doNothing().when(clientsBddService).delete(testUUID);

            // When
            clientsService.delete(testUUID);

            // Then
            verify(clientsBddService, times(1)).delete(testUUID);
        }

        @Test
        @DisplayName("Devrait appeler le service de suppression une seule fois")
        void shouldCallDeleteServiceOnce() {
            // When
            clientsService.delete(testUUID);

            // Then
            verify(clientsBddService, times(1)).delete(testUUID);
        }
    }
}

