package com.wgt.imt.architecture.business.clients.model;

import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Modèle métier représentant un client.
 * Contient les informations personnelles du client et ses comptes bancaires.
 *
 * Ces attributs sont fortement typés contrairement à la couche présentation.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Client {
    
    /**
     * Identifiant unique du client, généré automatiquement
     */
    @Builder.Default
    private final UUID identifier = UUID.randomUUID();
    
    /**
     * Nom de famille du client
     */
    private final String lastname;
    
    /**
     * Prénom du client
     */
    private final String firstname;
    
    /**
     * Genre du client (HOMME, FEMME, NON_COMMUNIQUER)
     */
    private final GenreEnum genre;
    
    /**
     * Collection des comptes bancaires associés au client
     */
    @Builder.Default
    private final Collection<Compte> comptes = Collections.emptySet();
}
