package com.wgt.imt.architecture.infrastructures.events.comptes.model;

import com.wgt.imt.architecture.business.common.model.MouvementCompteType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.UUID;

/**
 * Événement publié après un mouvement sur un compte.
 * Contient les détails utiles au traitement et à la traçabilité du mouvement.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class MouvementCompteEvent extends ApplicationEvent {
    
    @Serial
    private static final long serialVersionUID = -5205862949120431824L;
    
    /**
     * Identifiant du client propriétaire du compte
     */
    private final UUID clientIdentifier;
    
    /**
     * Identifiant du compte sur lequel le mouvement a eu lieu
     */
    private final UUID compteIdentifier;

    /**
     * Description du mouvement effectué
     */
    private final String message;

    /**
     * Origine du mouvement
     */
    private final MouvementCompteType type;
    
    /**
     * Constructeur de l'événement.
     *
     * @param source           la source de l'événement (généralement le publisher)
     * @param clientIdentifier l'identifiant du client
     * @param compteIdentifier l'identifiant du compte
     * @param message description du mouvement
     * @param type origine du mouvement
     */
    public MouvementCompteEvent(
            final Object source,
            final UUID clientIdentifier,
            final UUID compteIdentifier,
            final String message,
            final MouvementCompteType type
    ) {
        super(source);
        this.clientIdentifier = clientIdentifier;
        this.compteIdentifier = compteIdentifier;
        this.message = message;
        this.type = type;
    }
}
