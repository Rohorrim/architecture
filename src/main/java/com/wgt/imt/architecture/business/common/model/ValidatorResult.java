package com.wgt.imt.architecture.business.common.model;

import com.wgt.imt.architecture.business.common.exception.ValidationException;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.exception.ValidationException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Représente le résultat d'une validation.
 * Encapsule l'état de validation (valide/invalide) et l'exception à lever si invalide.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class ValidatorResult {
    
    /**
     * Crée un résultat de validation valide.
     *
     * @return un ValidatorResult marqué comme valide
     */
    public static ValidatorResult valid() {
        return ValidatorResult.builder().isValid(true).build();
    }
    
    /**
     * Crée un résultat de validation invalide avec une exception spécifique.
     *
     * @param exceptionToThrow l'exception à lever
     * @return un ValidatorResult marqué comme invalide avec l'exception fournie
     */
    public static ValidatorResult invalid(final ValidationException exceptionToThrow) {
        return ValidatorResult.builder().isValid(false).exceptionToThrow(exceptionToThrow).build();
    }
    
    /**
     * Indique si la validation est réussie
     */
    private final boolean isValid;
    /**
     * L'exception à lever si la validation a échoué
     */
    private final ValidationException exceptionToThrow;
    
    /**
     * Lève l'exception associée si la validation a échoué.
     * Ne fait rien si la validation est valide.
     *
     * @throws ValidationException si la validation a échoué
     */
    public void throwIfInvalid(){
        if(!this.isValid()){
            throw this.exceptionToThrow;
        }
    }
}
