package com.wgt.imt.architecture.business.clients;

import com.wgt.imt.architecture.business.clients.model.Client;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ClientsService {
    final Map<UUID, Client> clients = new HashMap<>();
    
    /**
     * Récupère tous les clients.
     *
     * @return une collection de tous les clients, ou une collection vide si aucun client n'existe
     */
    public Collection<Client> getAll() {
        return this.clients.values();
    }
    
    /**
     * Récupère un client spécifique par son identifiant.
     *
     * @param identifier l'identifiant unique du client
     * @return un Optional contenant le client s'il existe, Optional.empty() sinon
     */
    public Optional<Client> getOne(final UUID identifier) {
        return Optional.ofNullable(this.clients.get(identifier));
    }
    
    /**
     * Crée un nouveau client.
     *
     * @param newClient le nouveau client à créer
     * @return le client créé avec son identifiant généré
     */
    public Client create(final Client newClient) {
        this.clients.put(newClient.getIdentifier(), newClient);
        return newClient;
    }
    
    /**
     * Met à jour un client existant.
     *
     * @param updatedClient le client avec les informations mises à jour
     */
    public void update(final Client updatedClient) {
        this.clients.put(updatedClient.getIdentifier(), updatedClient);
    }
    
    /**
     * Supprime un client par son identifiant.
     *
     * @param identifier l'identifiant unique du client à supprimer
     */
    public void delete(final UUID identifier) {
        this.clients.remove(identifier);
    }
}
