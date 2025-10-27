package com.wgt.imt.architecture.infrastructures.bdd.clients;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.ClientRepository;
import com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.entities.ClientEntity;
import com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.mappers.ClientBddMapper;
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
@DisplayName("ClientsBddService")
class ClientsBddServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientBddMapper clientBddMapper;

    @InjectMocks
    private ClientsBddService clientsBddService;

    private UUID testUUID;
    private Client testClient;
    private ClientEntity testClientEntity;

    @BeforeEach
    void setUp() {
        testUUID = UUID.randomUUID();

        testClient = Client.builder()
                .identifier(testUUID)
                .lastname("Dupont")
                .firstname("Jean")
                .genre(GenreEnum.HOMME)
                .build();

        testClientEntity = new ClientEntity();
    }

    @Nested
    @DisplayName("exist(UUID)")
    class Exist {

        @Test
        @DisplayName("Devrait retourner true si le client existe")
        void shouldReturnTrueWhenClientExists() {
            // Given
            when(clientRepository.existsById(testUUID.toString())).thenReturn(true);

            // When
            boolean result = clientsBddService.exist(testUUID);

            // Then
            assertTrue(result);
            verify(clientRepository, times(1)).existsById(testUUID.toString());
        }

        @Test
        @DisplayName("Devrait retourner false si le client n'existe pas")
        void shouldReturnFalseWhenClientDoesNotExist() {
            // Given
            when(clientRepository.existsById(testUUID.toString())).thenReturn(false);

            // When
            boolean result = clientsBddService.exist(testUUID);

            // Then
            assertFalse(result);
            verify(clientRepository, times(1)).existsById(testUUID.toString());
        }

        @Test
        @DisplayName("Devrait retourner false si l'UUID est null")
        void shouldReturnFalseWhenUUIDIsNull() {
            // When
            boolean result = clientsBddService.exist(null);

            // Then
            assertFalse(result);
            verify(clientRepository, never()).existsById(any());
        }
    }

    @Nested
    @DisplayName("getAll()")
    class GetAll {

        @Test
        @DisplayName("Devrait retourner tous les clients")
        void shouldReturnAllClients() {
            // Given
            List<ClientEntity> entities = Arrays.asList(testClientEntity);
            when(clientRepository.findAll()).thenReturn(entities);
            when(clientBddMapper.from(testClientEntity)).thenReturn(testClient);

            // When
            Collection<Client> result = clientsBddService.getAll();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(clientRepository, times(1)).findAll();
            verify(clientBddMapper, times(1)).from(testClientEntity);
        }

        @Test
        @DisplayName("Devrait retourner une collection vide si aucun client")
        void shouldReturnEmptyCollectionWhenNoClients() {
            // Given
            when(clientRepository.findAll()).thenReturn(Collections.emptyList());

            // When
            Collection<Client> result = clientsBddService.getAll();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(clientRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Devrait gérer le cas où findAll retourne null")
        void shouldHandleNullFromFindAll() {
            // Given
            when(clientRepository.findAll()).thenReturn(null);

            // When
            Collection<Client> result = clientsBddService.getAll();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("get(UUID)")
    class Get {

        @Test
        @DisplayName("Devrait retourner un client par son UUID")
        void shouldReturnClientByUUID() {
            // Given
            when(clientRepository.findById(testUUID.toString())).thenReturn(Optional.of(testClientEntity));
            when(clientBddMapper.from(testClientEntity)).thenReturn(testClient);

            // When
            Optional<Client> result = clientsBddService.get(testUUID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(testClient, result.get());
            verify(clientRepository, times(1)).findById(testUUID.toString());
            verify(clientBddMapper, times(1)).from(testClientEntity);
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty si le client n'existe pas")
        void shouldReturnEmptyOptionalWhenClientNotFound() {
            // Given
            when(clientRepository.findById(testUUID.toString())).thenReturn(Optional.empty());

            // When
            Optional<Client> result = clientsBddService.get(testUUID);

            // Then
            assertFalse(result.isPresent());
            verify(clientRepository, times(1)).findById(testUUID.toString());
            verify(clientBddMapper, never()).from(any());
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty si l'UUID est null")
        void shouldReturnEmptyOptionalWhenUUIDIsNull() {
            // When
            Optional<Client> result = clientsBddService.get(null);

            // Then
            assertFalse(result.isPresent());
            verify(clientRepository, never()).findById(any());
        }
    }

    @Nested
    @DisplayName("save(Client)")
    class Save {

        @Test
        @DisplayName("Devrait sauvegarder un client et le retourner")
        void shouldSaveClientAndReturnIt() {
            // Given
            when(clientBddMapper.to(testClient)).thenReturn(testClientEntity);
            when(clientRepository.save(testClientEntity)).thenReturn(testClientEntity);
            when(clientBddMapper.from(testClientEntity)).thenReturn(testClient);

            // When
            Client result = clientsBddService.save(testClient);

            // Then
            assertNotNull(result);
            assertEquals(testClient, result);
            verify(clientBddMapper, times(1)).to(testClient);
            verify(clientRepository, times(1)).save(testClientEntity);
            verify(clientBddMapper, times(1)).from(testClientEntity);
        }

        @Test
        @DisplayName("Devrait lever NullPointerException si le client est null")
        void shouldThrowNullPointerExceptionWhenClientIsNull() {
            // When & Then
            assertThrows(NullPointerException.class,
                () -> clientsBddService.save(null));

            verify(clientRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete(UUID)")
    class Delete {

        @Test
        @DisplayName("Devrait supprimer un client par son UUID")
        void shouldDeleteClientByUUID() {
            // Given
            doNothing().when(clientRepository).deleteById(testUUID.toString());

            // When
            clientsBddService.delete(testUUID);

            // Then
            verify(clientRepository, times(1)).deleteById(testUUID.toString());
        }

        @Test
        @DisplayName("Ne devrait rien faire si l'UUID est null")
        void shouldDoNothingWhenUUIDIsNull() {
            // When
            clientsBddService.delete(null);

            // Then
            verify(clientRepository, never()).deleteById(any());
        }
    }
}

