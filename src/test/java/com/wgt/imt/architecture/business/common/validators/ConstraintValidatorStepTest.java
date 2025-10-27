package com.wgt.imt.architecture.business.common.validators;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConstraintValidatorStep")
class ConstraintValidatorStepTest {

    private final ConstraintValidatorStep<Client> validator = new ConstraintValidatorStep<>();

    @Test
    @DisplayName("Devrait accepter un objet conforme aux contraintes")
    void shouldAcceptValidObject() {
        Client client = Client.builder()
                .lastname("Dupont")
                .firstname("Jean")
                .genre(GenreEnum.HOMME)
                .build();

        assertDoesNotThrow(() -> validator.check(client));
    }

    @Test
    @DisplayName("Devrait rejeter un objet avec plusieurs contraintes non respectées")
    void shouldRejectObjectWithConstraintViolations() {
        Client client = Client.builder()
                .lastname("Dupont1")
                .firstname(null)
                .genre(GenreEnum.HOMME)
                .build();

        ConstraintValidationException exception = assertThrows(
                ConstraintValidationException.class,
                () -> validator.check(client)
        );

        assertTrue(exception.getMessage().contains("Le nom n'est pas valide"));
        assertTrue(exception.getMessage().contains("Le prenom ne peut pas être nul"));
    }
}
