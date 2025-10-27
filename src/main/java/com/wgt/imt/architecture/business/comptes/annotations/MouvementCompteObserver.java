package com.wgt.imt.architecture.business.comptes.annotations;

import com.wgt.imt.architecture.business.common.model.MouvementCompteType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Métadonnées de l'événement publié après la réussite de la méthode annotée.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MouvementCompteObserver {
    /**
     * Description lisible du mouvement.
     */
    String message();

    /**
     * Origine du mouvement.
     */
    MouvementCompteType type();
    
    /**
     * Expression SpEL donnant l'identifiant du client, par exemple {@code #p1.identifier}
     * pour un argument de type {@code Client} ou {@code #p1} pour un UUID.
     */
    String clientIdentifierExpression() default "#p0";

    /**
     * Expression SpEL donnant l'identifiant du compte, par exemple {@code #p1.identifier}
     * pour un argument de type {@code Compte} ou {@code #p1} pour un UUID.
     */
    String compteIdentifierExpression();

    /**
     * Condition SpEL évaluée après le retour de la méthode. La publication a lieu par défaut.
     */
    String doesPublish() default "true";
}
