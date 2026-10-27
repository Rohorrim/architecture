package com.wgt.imt.architecture.business.comptes;

import com.wgt.imt.architecture.business.common.exception.ConflictValidationException;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.validators.ConstraintValidatorStep;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.business.comptes.validators.CompteAlreadyExistValidatorStep;
import com.wgt.imt.architecture.business.comptes.validators.CompteTypeValidatorStep;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service métier de gestion des comptes avec validation.
 * Étend ComptesService en ajoutant des validations métier avant les opérations de création et modification.
 * Utilise le pattern Chain of Responsibility pour enchaîner les validations.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Service
public class ComptesServiceValidator extends ComptesService {

    public ComptesServiceValidator(final ClientsBddService service) {
        super(service);
    }
    
    /**
     * Crée un nouveau compte après validation.
     * Valide que :
     * - Les contraintes sur l'objet sont respectées
     * - Le compte n'existe pas déjà
     * - Le type de compte est valide
     *
     * @param newCompte le nouveau compte à créer
     * @return le compte créé
     * @throws ConstraintValidationException si les contraintes ne sont pas respectées
     * @throws ConflictValidationException   si le compte existe déjà
     */
    public Compte create(final UUID clientIdentifier, final Compte newCompte) {
        new ConstraintValidatorStep<Compte>()
                .linkWith(new CompteAlreadyExistValidatorStep(this.service, clientIdentifier))
                .linkWith(new CompteTypeValidatorStep())
                .validate(newCompte)
                .throwIfInvalid();

        return super.create(clientIdentifier, newCompte);
    }
    
    /**
     * Met à jour un compte existant après validation.
     * Valide que :
     * - Les contraintes sur l'objet sont respectées
     * - Le type de compte est valide
     *
     * @param updatedCompte le compte avec les informations mises à jour
     * @throws ConstraintValidationException si les contraintes ne sont pas respectées
     */
    public void update(final UUID clientIdentifier, final Compte updatedCompte) {
        new ConstraintValidatorStep<Compte>()
                .linkWith(new CompteTypeValidatorStep())
                .validate(updatedCompte)
                .throwIfInvalid();

        super.update(clientIdentifier, updatedCompte);
    }
}
