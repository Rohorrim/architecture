package com.wgt.imt.architecture.business.comptes;

import com.wgt.imt.architecture.business.common.exception.NotFoundValidationException;
import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.comptes.annotations.MouvementCompteObserver;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.business.common.model.MouvementCompteType;
import com.wgt.imt.architecture.business.comptes.model.Mouvement;
import com.wgt.imt.architecture.business.common.utils.CollectionImtUtils;
import com.wgt.imt.architecture.infrastructures.bdd.clients.ClientsBddService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Service
@AllArgsConstructor
public class ComptesService {
    private static final int MONTANT_AGIOS = 5;
    
    /**
     * Service d'accès à la base de données pour les clients
     */
    protected final ClientsBddService service;
    
    /**
     * Récupère tous les comptes associés à un client donné.
     *
     * @param clientIdentifier Identifiant du client
     * @return Collection des comptes du client, ou une collection vide si le client n'existe pas
     */
    public Collection<Compte> getAllFilteredByClient(final UUID clientIdentifier) {
        log.info("Récupération du tous les comptes pour le client avec l'identifiant : {}", clientIdentifier);
        return this.service.get(clientIdentifier).map(Client::getComptes).orElse(Collections.emptySet());
    }
    
    /**
     * Récupère un compte spécifique d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param identifier       Identifiant du compte
     * @return Un Optional contenant le compte s'il existe, sinon vide
     */
    public Optional<Compte> getOneFilteredByClient(final UUID clientIdentifier, final UUID identifier) {
        log.info("Récupération du compte avec l'identifiant : {} pour le client avec l'identifiant : {}", identifier, clientIdentifier);
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
     * @param newCompte        Nouveau compte à ajouter
     * @return Le compte créé
     * @throws NotFoundValidationException si le client n'existe pas
     */
    @MouvementCompteObserver(message = "Création du compte", type = MouvementCompteType.UTILISATEUR, compteIdentifierExpression = "#p1.identifier")
    public Compte create(final UUID clientIdentifier, final Compte newCompte) {
        log.info("Création d'un nouveau compte pour le client avec l'identifiant : {}", clientIdentifier);
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        this.service.save(client.toBuilder()
                                .comptes(CollectionImtUtils.append(client.getComptes(), newCompte))
                                .build()); return newCompte;
    }
    
    /**
     * Met à jour un compte existant d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param updatedCompte    Compte mis à jour
     * @throws NotFoundValidationException si le client n'existe pas
     */
    @MouvementCompteObserver(message = "Mise à jour du compte", type = MouvementCompteType.UTILISATEUR, compteIdentifierExpression = "#p1.identifier")
    public void update(final UUID clientIdentifier, final Compte updatedCompte) {
        log.info("Mise à jour du compte {} pour le client avec l'identifiant : {}", updatedCompte.getIdentifier(), clientIdentifier);
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        this.update(client, updatedCompte);
    }
    
    /**
     * Applique les agios sur le compte d'un client si le solde est négatif.
     *
     * @param clientIdentifier Identifiant du client
     * @param identifier       Identifiant du compte
     * @throws NotFoundValidationException si le client n'existe pas
     */
    @MouvementCompteObserver(message = "Application automatique des agios", type = MouvementCompteType.AUTOMATIQUE, compteIdentifierExpression = "#p1", doesPublish = "#result")
    public boolean applyAgiosIfNeeded(final UUID clientIdentifier, final UUID identifier) {
        log.info("Vérification de l'application des agios pour le compte {} du client avec l'identifiant : {}", identifier, clientIdentifier);
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        return Objects.requireNonNullElse(client.getComptes(), Collections.<Compte>emptySet()).stream()
                      .filter(finded -> Objects.equals(finded.getIdentifier(), identifier) && finded.getSolde() < 0)
                      .findFirst()
                      .map(finded -> {
                          this.update(client, finded.toBuilder().solde(finded.getSolde() - MONTANT_AGIOS).build());
                          return true;
                      })
                      .orElse(false);
    }
    
    /**
     * Ajoute un message dans l'historique d'un compte d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param compteIdentifier Identifiant du compte
     * @param message          Message à ajouter dans l'historique
     * @throws NotFoundValidationException si le client n'existe pas
     */
    public void addIntoHistory(final UUID clientIdentifier, final UUID compteIdentifier, final String message) {
        log.info("Ajout d'un message dans l'historique du compte {} pour le client avec l'identifiant : {}", compteIdentifier, clientIdentifier);
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        Objects.requireNonNullElse(client.getComptes(), Collections.<Compte>emptySet()).stream()
                      .filter(finded -> Objects.equals(finded.getIdentifier(), compteIdentifier))
                      .findFirst()
                      .ifPresent(finded -> this.update(client, finded.toBuilder()
                                    .historique(CollectionImtUtils.append(finded.getHistorique(), Mouvement.builder().message(message).build()))
                                    .build())
                      );
    }
    
    /**
     * Supprime un compte d'un client.
     *
     * @param clientIdentifier Identifiant du client
     * @param identifier       Identifiant du compte à supprimer
     * @throws NotFoundValidationException si le client n'existe pas
     */
    public void delete(final UUID clientIdentifier, final UUID identifier) {
        log.info("Suppression du compte {} pour le client avec l'identifiant : {}", identifier, clientIdentifier);
        final Client client = this.service.get(clientIdentifier).orElseThrow(() -> new NotFoundValidationException(String.format("Le client %s n'existe pas", clientIdentifier)));
        this.service.save(client.toBuilder().comptes(
                Objects.requireNonNullElse(client.getComptes(), Collections.<Compte>emptySet())
                       .stream()
                       .filter(compte -> !Objects.equals(compte.getIdentifier(), identifier))
                       .collect(Collectors.toSet())
        ).build());
    }
    
    /**
     * Met à jour un compte existant d'un client.
     *
     * @param client        Client dont le compte doit être mis à jour
     * @param updatedCompte Compte mis à jour
     */
    private void update(final Client client, final Compte updatedCompte) {
        this.service.save(client.toBuilder().comptes(
                CollectionImtUtils.append(Objects.requireNonNullElse(client.getComptes(), Collections.<Compte>emptySet()).stream()
                          .filter(compte -> !Objects.equals(compte.getIdentifier(), updatedCompte.getIdentifier()))
                                                 .collect(Collectors.toSet()), updatedCompte)
        ).build());
    }
}
