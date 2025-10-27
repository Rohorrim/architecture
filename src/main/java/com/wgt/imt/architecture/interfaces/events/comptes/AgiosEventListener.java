package com.wgt.imt.architecture.interfaces.events.comptes;

import com.wgt.imt.architecture.business.comptes.ComptesService;
import com.wgt.imt.architecture.business.common.model.MouvementCompteType;
import com.wgt.imt.architecture.infrastructures.events.comptes.model.MouvementCompteEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Écouteur d'événements pour les mouvements de compte.
 * L'exécution est asynchrone pour ne pas bloquer le traitement principal.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Log4j2
@Service
@AllArgsConstructor
public class AgiosEventListener implements ApplicationListener<MouvementCompteEvent> {
    
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
        if(Objects.nonNull(event.getType()) && !MouvementCompteType.AUTOMATIQUE.equals(event.getType())) {
            if(this.comptesService.applyAgiosIfNeeded(event.getClientIdentifier(), event.getCompteIdentifier())) {
                log.info("Agios appliqués avec succès pour le compte : {}", event.getCompteIdentifier());
            } else {
                log.info("Aucun agios à appliquer pour le compte : {}", event.getCompteIdentifier());
            }
        }
    }
}
