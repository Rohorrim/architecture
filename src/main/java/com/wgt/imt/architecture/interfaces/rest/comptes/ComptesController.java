package com.wgt.imt.architecture.interfaces.rest.comptes;

import com.wgt.imt.architecture.interfaces.rest.comptes.model.input.CompteInput;
import com.wgt.imt.architecture.interfaces.rest.comptes.model.input.CompteUpdateInput;
import com.wgt.imt.architecture.interfaces.rest.comptes.model.output.ComptesOutput;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Contrôleur REST pour la gestion des comptes bancaires.
 * Expose-les endpoints HTTP pour les opérations CRUD sur les comptes d'un client.
 * Point d'entrée : /api/imt/v1/clients/{idClient}/comptes
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/imt/v1/clients/{idClient}/comptes")
public class ComptesController {
    
    /**
     * Récupère tous les comptes d'un client.
     *
     * @param clientIdentifier l'identifiant du client au format String
     * @return une collection de ComptesOutput représentant tous les comptes du client
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<ComptesOutput> getAll(@PathVariable("idClient") final String clientIdentifier) {
        return Set.of(
                ComptesOutput.builder()
                        .identifier(UUID.randomUUID().toString())
                        .name("Compte Courant")
                        .type("CC")
                        .solde(1500.0)
                        .build(),
                ComptesOutput.builder()
                        .identifier(UUID.randomUUID().toString())
                        .name("Livret A")
                        .type("LA")
                        .solde(3000.0)
                        .build()
        );
    }
    
    /**
     * Crée un nouveau compte pour un client.
     *
     * @param clientIdentifier l'identifiant du client au format String
     * @param compte           les informations du compte à créer
     * @return le compte créé avec son identifiant généré
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ComptesOutput create(@PathVariable("idClient") final String clientIdentifier, @RequestBody final CompteInput compte) {
        return ComptesOutput.builder()
                .identifier(UUID.randomUUID().toString())
                .name(compte.getName())
                .type(compte.getType())
                .solde(0.0)
                .build();
    }
    
    /**
     * Récupère un compte spécifique d'un client.
     *
     * @param clientIdentifier l'identifiant du client au format String
     * @param identifier       l'identifiant du compte au format String
     * @return le compte correspondant
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{idCompte}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ComptesOutput getOne(@PathVariable("idClient") final String clientIdentifier, @PathVariable("idCompte") final String identifier) {
        return ComptesOutput.builder()
                .identifier(identifier)
                .name("Livret A")
                .type("LA")
                .solde(3000.0)
                .build();
    }
    
    /**
     * Met à jour partiellement un compte existant (PATCH).
     *
     * @param clientIdentifier l'identifiant du client au format String
     * @param identifier       l'identifiant du compte au format String
     * @param compte           les informations à mettre à jour
     */
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{idCompte}")
    public void update(@PathVariable("idClient") final String clientIdentifier, @PathVariable("idCompte") final String identifier, @RequestBody final CompteUpdateInput compte) {

    }
    
    /**
     * Supprime un compte d'un client.
     *
     * @param clientIdentifier l'identifiant du client au format String
     * @param identifier       l'identifiant du compte au format String
     */
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idCompte}")
    public void delete(@PathVariable("idClient") final String clientIdentifier, @PathVariable("idCompte") final String identifier) {

    }
}
