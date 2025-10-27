package com.wgt.imt.architecture.interfaces.events.comptes;

import com.wgt.imt.architecture.business.comptes.ComptesService;
import com.wgt.imt.architecture.infrastructures.events.comptes.model.MouvementCompteEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Écouteur d'événements pour les mouvements de compte.
 * L'exécution est asynchrone pour ne pas bloquer le traitement principal.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class MouvementCompteEventListener implements ApplicationListener<MouvementCompteEvent> {
    
    /**
     * Service métier pour la gestion des comptes
     */
    private final ComptesService comptesService;
    
    /**
     * Traite les événements de mouvement de compte non automatiques en appliquant les agios si nécessaire.
     * Méthode exécutée de manière asynchrone.
     *
     * @param event l'événement contenant les informations du compte
     */
    @Async
    @Override
    public void onApplicationEvent(final MouvementCompteEvent event) {
        this.comptesService.addIntoHistory(event.getClientIdentifier(), event.getCompteIdentifier(), event.getMessage());
    }
}
