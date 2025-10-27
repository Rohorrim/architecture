package com.wgt.imt.architecture.business.clients;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.clients.validators.ClientAlreadyExistValidatorStep;
import com.wgt.imt.architecture.business.clients.validators.ClientGenreValidatorStep;
import com.wgt.imt.architecture.business.common.exception.ConflictValidationException;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.validators.ConstraintValidatorStep;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Service métier de gestion des clients avec validation.
 * Étend ClientsService en ajoutant des validations métier avant les opérations de création et modification.
 * Utilise le pattern Chain of Responsibility pour enchaîner les validations.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Log4j2
@Service
public class ClientsServiceValidator extends ClientsService {
    
    /**
     * Constructeur du service de validation des clients.
     *
     * @param service le service d'accès à la base de données pour les clients
     */
    public ClientsServiceValidator(final ClientsBddService service) {
        super(service);
    }
    
    /**
     * Crée un nouveau client après validation.
     * Valide que :
     * - Les contraintes sur l'objet sont respectées
     * - Le client n'existe pas déjà
     * - Le genre est valide
     *
     * @param newClient le nouveau client à créer
     * @return le client créé
     * @throws ConstraintValidationException si les contraintes ne sont pas respectées
     * @throws ConflictValidationException   si le client existe déjà
     */
    public Client create(final Client newClient) {
        log.info("Validation du nouveau client avant création : {}", newClient);
        new ConstraintValidatorStep<Client>()
                .linkWith(new ClientAlreadyExistValidatorStep(this.service))
                .linkWith(new ClientGenreValidatorStep())
                .validate(newClient)
                .throwIfInvalid();
        
        return super.create(newClient);
    }
    
    /**
     * Met à jour un client existant après validation.
     * Valide que :
     * - Les contraintes sur l'objet sont respectées
     * - Le genre est valide
     *
     * @param updatedClient le client avec les informations mises à jour
     * @throws ConstraintValidationException si les contraintes ne sont pas respectées
     */
    public void update(final Client updatedClient) {
        log.info("Validation du client avant mise à jour : {}", updatedClient);
        new ConstraintValidatorStep<Client>()
                .linkWith(new ClientGenreValidatorStep())
                .validate(updatedClient)
                .throwIfInvalid();
        
        super.update(updatedClient);
    }
}
