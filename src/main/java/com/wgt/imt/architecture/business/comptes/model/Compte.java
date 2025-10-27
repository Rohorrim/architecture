package com.wgt.imt.architecture.business.comptes.model;

import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

/**
 * Modèle métier représentant un compte bancaire.
 * Contient les informations du compte incluant le type, le nom et le solde.
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
public class Compte {
    
    /**
     * Identifiant unique du compte, généré automatiquement
     */
    @Builder.Default
    private final UUID identifier = UUID.randomUUID();
    
    /**
     * Identifiant du client propriétaire du compte
     */
    @Builder.Default
    private final UUID clientIdentifier = UUID.randomUUID();
    
    /**
     * Nom du compte
     */
    private final String name;
    
    /**
     * Type de compte (COMPTE_COURANT, LIVRET_A, LDDS)
     */
    private final TypeCompteEnum type;
    
    /**
     * Solde du compte, par défaut à 0
     */
    @Builder.Default
    private final Double solde = 0D;
}
