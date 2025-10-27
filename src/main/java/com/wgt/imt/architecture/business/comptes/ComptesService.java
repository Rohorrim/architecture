package com.wgt.imt.architecture.business.comptes;

import com.wgt.imt.architecture.business.common.exception.NotFoundValidationException;
import com.wgt.imt.architecture.business.comptes.model.Compte;
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
    final Map<UUID, Compte> comptes = new HashMap<>();
    
    /**
     * Récupère tous les comptes associés à un client donné.
     *
     * @param clientIdentifier Identifiant du client
     * @return Collection des comptes du client, ou une collection vide si le client n'existe pas
     */
    public Collection<Compte> getAllFilteredByClient(final UUID clientIdentifier) {
        return Objects.requireNonNullElse(this.comptes.values(), Collections.<Compte>emptySet()).stream()
                .filter(compte -> Objects.equals(compte.getClientIdentifier(), clientIdentifier))
                .collect(Collectors.toSet());
    }
    
    /**
     * Récupère un compte spécifique d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param identifier Identifiant du compte
     * @return Un Optional contenant le compte s'il existe, sinon vide
     */
    public Optional<Compte> getOneFilteredByClient(final UUID clientIdentifier, final UUID identifier) {
        return Optional.ofNullable(this.comptes.get(identifier))
                .filter(compte -> Objects.equals(compte.getClientIdentifier(), clientIdentifier));
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
        this.comptes.put(newCompte.getIdentifier(), newCompte);
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
        this.comptes.put(updatedCompte.getIdentifier(), updatedCompte);
    }
    
    
    /**
     * Supprime un compte d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param identifier Identifiant du compte à supprimer
     * @throws NotFoundValidationException si le client n'existe pas
     */
    public void delete(final UUID clientIdentifier, final UUID identifier) {
        this.comptes.remove(identifier);
    }
}
