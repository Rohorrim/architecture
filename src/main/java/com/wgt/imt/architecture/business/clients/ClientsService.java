package com.wgt.imt.architecture.business.clients;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@AllArgsConstructor
public class ClientsService {
    /**
     * Service d'accès à la base de données pour les clients
     */
    protected ClientsBddService service;
    
    /**
     * Récupère tous les clients.
     *
     * @return une collection de tous les clients, ou une collection vide si aucun client n'existe
     */
    public Collection<Client> getAll() {
        log.info("Récupération de tous les clients");
        return Objects.requireNonNullElse(this.service.getAll(), Collections.emptySet());
    }
    
    /**
     * Récupère un client spécifique par son identifiant.
     *
     * @param identifier l'identifiant unique du client
     * @return un Optional contenant le client s'il existe, Optional.empty() sinon
     */
    public Optional<Client> getOne(final UUID identifier) {
        log.info("Récupération du client avec l'identifiant : {}", identifier);
        return this.service.get(identifier);
    }
    
    /**
     * Crée un nouveau client.
     *
     * @param newClient le nouveau client à créer
     * @return le client créé avec son identifiant généré
     */
    public Client create(final Client newClient) {
        log.info("Création d'un nouveau client : {}", newClient);
        return this.service.save(newClient);
    }
    
    /**
     * Met à jour un client existant.
     *
     * @param updatedClient le client avec les informations mises à jour
     */
    public void update(final Client updatedClient) {
        log.info("Mise à jour du client : {}", updatedClient);
        this.service.save(updatedClient);
    }
    
    /**
     * Supprime un client par son identifiant.
     *
     * @param identifier l'identifiant unique du client à supprimer
     */
    public void delete(final UUID identifier) {
        log.info("Suppression du client avec l'identifiant : {}", identifier);
        this.service.delete(identifier);
    }
}
