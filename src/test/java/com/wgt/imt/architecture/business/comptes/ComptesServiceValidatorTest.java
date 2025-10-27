package com.wgt.imt.architecture.business.comptes;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.exception.ConflictValidationException;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.exception.NotFoundValidationException;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComptesServiceValidator")
class ComptesServiceValidatorTest {

    @Mock
    private ClientsBddService clientsBddService;

    private ComptesServiceValidator comptesService;
    private UUID clientIdentifier;
    private UUID compteIdentifier;
    private Compte compte;
    private Client client;

    @BeforeEach
    void setUp() {
        comptesService = new ComptesServiceValidator(clientsBddService);
        clientIdentifier = UUID.randomUUID();
        compteIdentifier = UUID.randomUUID();
        compte = Compte.builder()
                .identifier(compteIdentifier)
                .name("Compte_Courant")
                .type(TypeCompteEnum.COMPTE_COURANT)
                .solde(100.0)
                .build();
        client = Client.builder()
                .identifier(clientIdentifier)
                .lastname("Dupont")
                .firstname("Jean")
                .genre(GenreEnum.HOMME)
                .comptes(Collections.emptySet())
                .build();
    }

    @Test
    @DisplayName("Devrait valider et créer un compte pour un client existant")
    void shouldValidateAndCreateAccount() {
        when(clientsBddService.get(clientIdentifier)).thenReturn(Optional.of(client));
        when(clientsBddService.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(compte, comptesService.create(clientIdentifier, compte));

        verify(clientsBddService, times(2)).get(clientIdentifier);
        verify(clientsBddService).save(argThat(saved ->
                saved.getComptes().stream().anyMatch(compte::equals)
        ));
    }

    @Test
    @DisplayName("Ne devrait pas créer un compte aux champs invalides")
    void shouldRejectAccountWithInvalidConstraints() {
        Compte invalidCompte = compte.toBuilder().name("Compte invalide").build();

        assertThrows(ConstraintValidationException.class,
                () -> comptesService.create(clientIdentifier, invalidCompte));

        verify(clientsBddService, never()).save(any());
    }

    @Test
    @DisplayName("Ne devrait pas créer un compte avec un nom et un type déjà utilisés")
    void shouldRejectDuplicateAccount() {
        Compte existingCompte = compte.toBuilder().name("COMPTE_COURANT").build();
        Client clientWithAccount = client.toBuilder()
                .comptes(Collections.singleton(existingCompte))
                .build();
        when(clientsBddService.get(clientIdentifier)).thenReturn(Optional.of(clientWithAccount));

        assertThrows(ConflictValidationException.class,
                () -> comptesService.create(clientIdentifier, compte));

        verify(clientsBddService, never()).save(any());
    }

    @Test
    @DisplayName("Ne devrait pas créer un compte de type inconnu")
    void shouldRejectUnknownAccountType() {
        Compte invalidCompte = compte.toBuilder().type(TypeCompteEnum.INCONNU).build();
        when(clientsBddService.get(clientIdentifier)).thenReturn(Optional.of(client));

        assertThrows(ConstraintValidationException.class,
                () -> comptesService.create(clientIdentifier, invalidCompte));

        verify(clientsBddService, never()).save(any());
    }

    @Test
    @DisplayName("Devrait valider et mettre à jour un compte")
    void shouldValidateAndUpdateAccount() {
        when(clientsBddService.get(clientIdentifier)).thenReturn(Optional.of(client));

        comptesService.update(clientIdentifier, compte);

        verify(clientsBddService).save(argThat(saved ->
                saved.getComptes().stream().anyMatch(compte::equals)
        ));
    }

    @Test
    @DisplayName("Ne devrait pas mettre à jour un compte de type inconnu")
    void shouldRejectUpdateWithUnknownAccountType() {
        Compte invalidCompte = compte.toBuilder().type(TypeCompteEnum.INCONNU).build();

        assertThrows(ConstraintValidationException.class,
                () -> comptesService.update(clientIdentifier, invalidCompte));

        verifyNoInteractions(clientsBddService);
    }

    @Test
    @DisplayName("Devrait transmettre l'absence du client lors de la création")
    void shouldFailToCreateForMissingClient() {
        when(clientsBddService.get(clientIdentifier)).thenReturn(Optional.empty());

        assertThrows(NotFoundValidationException.class,
                () -> comptesService.create(clientIdentifier, compte));

        verify(clientsBddService, times(2)).get(clientIdentifier);
        verify(clientsBddService, never()).save(any());
    }
}
