package com.wgt.imt.architecture.business.comptes.validators;

import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CompteTypeValidatorStep")
class CompteTypeValidatorStepTest {

    private final CompteTypeValidatorStep validator = new CompteTypeValidatorStep();

    @Nested
    @DisplayName("check(Compte)")
    class Check {

        @Test
        @DisplayName("Devrait valider un compte avec le type COMPTE_COURANT")
        void shouldValidateAccountWithTypeCompteCourant() {
            // Given
            Compte compte = Compte.builder()
                    .name("Mon_Compte_Courant")
                    .type(TypeCompteEnum.COMPTE_COURANT)
                    .solde(100.0)
                    .build();

            // When & Then
            assertDoesNotThrow(() -> validator.check(compte));
        }

        @Test
        @DisplayName("Devrait valider un compte avec le type LIVRET_A")
        void shouldValidateAccountWithTypeLivretA() {
            // Given
            Compte compte = Compte.builder()
                    .name("Mon_Livret_A")
                    .type(TypeCompteEnum.LIVRET_A)
                    .solde(500.0)
                    .build();

            // When & Then
            assertDoesNotThrow(() -> validator.check(compte));
        }

        @Test
        @DisplayName("Devrait valider un compte avec le type LDDS")
        void shouldValidateAccountWithTypeLDDS() {
            // Given
            Compte compte = Compte.builder()
                    .name("Mon_LDDS")
                    .type(TypeCompteEnum.LDDS)
                    .solde(200.0)
                    .build();

            // When & Then
            assertDoesNotThrow(() -> validator.check(compte));
        }

        @Test
        @DisplayName("Devrait lever ConstraintValidationException si le type est INCONNU")
        void shouldThrowBusinessValidationExceptionWhenTypeIsInconnu() {
            // Given
            Compte compte = Compte.builder()
                    .name("Compte_Test")
                    .type(TypeCompteEnum.INCONNU)
                    .solde(0.0)
                    .build();

            // When & Then
            ConstraintValidationException exception = assertThrows(ConstraintValidationException.class,
                    () -> validator.check(compte));

            assertTrue(exception.getMessage().contains("type"));
            assertTrue(exception.getMessage().contains(TypeCompteEnum.ACCEPTABLE_VALUES));
        }
    }
}
