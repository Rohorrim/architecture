package com.wgt.imt.architecture.business.comptes;

import com.wgt.imt.architecture.business.common.exception.NotFoundValidationException;
import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.business.common.utils.CollectionImtUtils;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des comptes bancaires associés aux clients.
 * Permet de filtrer, créer, mettre à jour, appliquer des agios et supprimer des comptes.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ComptesService {
    /**
     * Service d'accès à la base de données pour les clients
     */
    private final ClientsBddService service;
    
    /**
     * Récupère tous les comptes associés à un client donné.
     *
     * @param clientIdentifier Identifiant du client
     * @return Collection des comptes du client, ou une collection vide si le client n'existe pas
     */
    public Collection<Compte> getAllFilteredByClient(final UUID clientIdentifier) {
        return this.service.get(clientIdentifier).map(Client::getComptes).orElse(Collections.emptySet());
    }
    
    /**
     * Récupère un compte spécifique d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param identifier Identifiant du compte
     * @return Un Optional contenant le compte s'il existe, sinon vide
     */
    public Optional<Compte> getOneFilteredByClient(final UUID clientIdentifier, final UUID identifier) {
        return this.service.get(clientIdentifier)
                .map(Client::getComptes)
                .stream()
                .flatMap(Collection::stream)
                .filter(compte -> Objects.equals(compte.getIdentifier(), identifier))
                .findFirst();
    }
    
    /**
     * Crée un nouveau compte pour un client donné.
     *
     * @param clientIdentifier Identifiant du client
     * @param newCompte Nouveau compte à ajouter
     * @return Le compte créé
     * @throws NotFoundValidationException si le client n'existe pas
     */
    public Compte create(final UUID clientIdentifier, final Compte newCompte) {
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        this.service.save(client.toBuilder().comptes(CollectionImtUtils.append(client.getComptes(), newCompte)).build());
        return newCompte;
    }
    
    /**
     * Met à jour un compte existant d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param updatedCompte Compte mis à jour
     * @throws NotFoundValidationException si le client n'existe pas
     */
    public void update(final UUID clientIdentifier, final Compte updatedCompte) {
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        this.service.save(
                client.toBuilder().comptes(CollectionImtUtils.append(
                        Objects.requireNonNullElse(client.getComptes(), Collections.<Compte>emptySet())
                                .stream()
                                .filter(compte -> !Objects.equals(compte.getIdentifier(), updatedCompte.getIdentifier()))
                                .collect(Collectors.toSet()),
                        updatedCompte
                )).build()
        );
    }
    
    
    /**
     * Supprime un compte d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param identifier Identifiant du compte à supprimer
     * @throws NotFoundValidationException si le client n'existe pas
     */
    public void delete(final UUID clientIdentifier, final UUID identifier) {
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        this.service.save(client.toBuilder().comptes(
                Objects.requireNonNullElse(client.getComptes(), Collections.<Compte>emptySet())
                        .stream()
                        .filter(compte -> !Objects.equals(compte.getIdentifier(), identifier))
                        .collect(Collectors.toSet())
        ).build());
    }
}
