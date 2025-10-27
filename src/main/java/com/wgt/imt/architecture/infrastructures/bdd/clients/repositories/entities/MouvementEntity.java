package com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité MongoDB représentant un compte bancaire.
 * Document embarqué dans ClientEntity (pas de collection séparée).
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MouvementEntity {
    
    /**
     * Identifiant unique du compte
     */
    private String identifier;
    
    /**
     * Date du mouvement
     */
    private LocalDateTime date;
    
    /**
     * Message du mouvement
     */
    private String message;
}
