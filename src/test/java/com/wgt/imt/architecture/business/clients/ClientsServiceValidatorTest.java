package com.wgt.imt.architecture.business.clients;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.exception.ConflictValidationException;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientsServiceValidator")
class ClientsServiceValidatorTest {

    @Mock
    private ClientsBddService clientsBddService;

    private ClientsServiceValidator clientsService;
    private Client client;

    @BeforeEach
    void setUp() {
        clientsService = new ClientsServiceValidator(clientsBddService);
        client = Client.builder()
                .lastname("Dupont")
                .firstname("Jean")
                .genre(GenreEnum.HOMME)
                .build();
    }

    @Test
    @DisplayName("Devrait valider et enregistrer un nouveau client")
    void shouldValidateAndCreateNewClient() {
        when(clientsBddService.getAll()).thenReturn(Collections.emptyList());
        when(clientsBddService.save(client)).thenReturn(client);

        assertEquals(client, clientsService.create(client));

        verify(clientsBddService).getAll();
        verify(clientsBddService).save(client);
    }

    @Test
    @DisplayName("Ne devrait pas enregistrer un client aux champs invalides")
    void shouldRejectClientWithInvalidConstraints() {
        Client invalidClient = client.toBuilder().lastname("Dupont1").build();

        assertThrows(ConstraintValidationException.class, () -> clientsService.create(invalidClient));

        verify(clientsBddService, never()).save(any());
    }

    @Test
    @DisplayName("Ne devrait pas enregistrer un client déjà existant")
    void shouldRejectDuplicateClient() {
        Client existingClient = client.toBuilder()
                .lastname("DUPONT")
                .firstname("JEAN")
                .build();
        when(clientsBddService.getAll()).thenReturn(List.of(existingClient));

        assertThrows(ConflictValidationException.class, () -> clientsService.create(client));

        verify(clientsBddService, never()).save(any());
    }

    @Test
    @DisplayName("Ne devrait pas enregistrer un client dont le genre est inconnu")
    void shouldRejectClientWithUnknownGender() {
        Client invalidClient = client.toBuilder().genre(GenreEnum.INCONNU).build();
        when(clientsBddService.getAll()).thenReturn(Collections.emptyList());

        assertThrows(ConstraintValidationException.class, () -> clientsService.create(invalidClient));

        verify(clientsBddService, never()).save(any());
    }

    @Test
    @DisplayName("Devrait valider et enregistrer un client modifié")
    void shouldValidateAndUpdateClient() {
        clientsService.update(client);

        verify(clientsBddService).save(client);
    }

    @Test
    @DisplayName("Ne devrait pas enregistrer un client modifié aux données invalides")
    void shouldRejectUpdateWithInvalidGender() {
        Client invalidClient = client.toBuilder().genre(GenreEnum.INCONNU).build();

        assertThrows(ConstraintValidationException.class, () -> clientsService.update(invalidClient));

        verifyNoInteractions(clientsBddService);
    }
}
