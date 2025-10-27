package com.wgt.imt.architecture.interfaces.rest.clients;

import com.wgt.imt.architecture.interfaces.rest.clients.model.input.ClientInput;
import com.wgt.imt.architecture.interfaces.rest.clients.model.input.ClientUpdateInput;
import com.wgt.imt.architecture.interfaces.rest.clients.model.output.ClientOutput;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Contrôleur REST pour la gestion des clients.
 * Expose les endpoints HTTP pour les opérations CRUD sur les clients.
 * Point d'entrée : /api/imt/v1/clients
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/imt/v1/clients")
public class ClientsController {
    
    /**
     * Récupère tous les clients.
     *
     * @return une collection de ClientOutput représentant tous les clients
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<ClientOutput> getAll() {
        return Set.of(
                ClientOutput.builder()
                        .identifier(UUID.randomUUID().toString())
                        .lastname("Doe")
                        .firstname("John")
                        .genre("M")
                        .build(),
                ClientOutput.builder()
                        .identifier(UUID.randomUUID().toString())
                        .lastname("Smith")
                        .firstname("Jane")
                        .genre("F")
                        .build()
        );
    }
    
    /**
     * Crée un nouveau client.
     *
     * @param client les informations du client à créer
     * @return le client créé avec son identifiant généré
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientOutput create(@RequestBody final ClientInput client) {
        return ClientOutput.builder()
                .identifier(UUID.randomUUID().toString())
                .lastname(client.getLastname())
                .firstname(client.getFirstname())
                .genre(client.getGenre())
                .build();
    }
    
    /**
     * Récupère un client spécifique par son identifiant.
     *
     * @param identifier l'identifiant du client au format String
     * @return le client correspondant
     */
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientOutput getOne(@PathVariable("idClient") final String identifier) {
        return ClientOutput.builder()
                .identifier(identifier)
                .lastname("Smith")
                .firstname("Jane")
                .genre("F")
                .build();
    }
    
    /**
     * Met à jour partiellement un client existant (PATCH).
     *
     * @param identifier l'identifiant du client au format String
     * @param client     les informations à mettre à jour
     */
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{idClient}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable("idClient") final String identifier, @RequestBody final ClientUpdateInput client) {

    }
    
    /**
     * Supprime un client par son identifiant.
     *
     * @param identifier l'identifiant du client au format String
     */
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idClient}")
    public void delete(@PathVariable("idClient") final String identifier) {

    }
}
