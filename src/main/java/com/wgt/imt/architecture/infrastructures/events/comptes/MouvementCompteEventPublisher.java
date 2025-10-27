package com.wgt.imt.architecture.infrastructures.events.comptes;

import com.wgt.imt.architecture.business.comptes.annotations.MouvementCompteObserver;
import com.wgt.imt.architecture.infrastructures.events.comptes.model.MouvementCompteEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

/**
 * Publie l'événement de mouvement après le succès d'une méthode observée.
 */
@Log4j2
@Aspect
@Component
@AllArgsConstructor
public class MouvementCompteEventPublisher {
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    
    /**
     * Publisher d'événements Spring
     */
    private final ApplicationEventPublisher publisher;
    
    /**
     * Publie l'événement lorsqu'un mouvement est détecté.
     *
     * @param joinPoint invocation de la méthode observée
     * @param observer métadonnées de l'événement à publier
     */
    @AfterReturning(
            value = "@annotation(observer)",
            returning = "result",
            argNames = "joinPoint,observer,result"
    )
    public void publierMouvement(final JoinPoint joinPoint, final MouvementCompteObserver observer, final Object result) {
        if (!this.evaluerCondition(joinPoint, observer, result)) {
            return;
        }

        log.info("Publication de l'événement de mouvement pour le compte : {} du client : {} avec le message : {} et le type : {}", this.extractCompteIdentifier(joinPoint, observer), this.extractClientIdentifier(joinPoint, observer), observer.message(), observer.type());
        this.publisher.publishEvent(new MouvementCompteEvent(
                this,
                this.extractClientIdentifier(joinPoint, observer),
                this.extractCompteIdentifier(joinPoint, observer),
                observer.message(),
                observer.type()
        ));
    }

    private boolean evaluerCondition(
            final JoinPoint joinPoint,
            final MouvementCompteObserver observer,
            final Object result
    ) {
        final Boolean condition = EXPRESSION_PARSER
                .parseExpression(observer.doesPublish())
                .getValue(this.createEvaluationContext(joinPoint, result), Boolean.class);
        return Objects.requireNonNull(condition, "La condition SpEL de publication a renvoyé null");
    }
    
    /**
     * Extrait l'identifiant du client à partir des arguments de la méthode observée.
     *
     * @param joinPoint invocation de la méthode observée
     * @param observer métadonnées définies sur la méthode
     * @return l'identifiant du client
     */
    private UUID extractClientIdentifier(final JoinPoint joinPoint, final MouvementCompteObserver observer) {
        return this.extractIdentifier(joinPoint, observer.clientIdentifierExpression());
    }
    
    /**
     * Évalue l'expression SpEL dans le contexte des arguments de la méthode observée.
     *
     * @param joinPoint invocation de la méthode observée
     * @param observer métadonnées définies sur la méthode
     * @return l'identifiant du compte
     */
    private UUID extractCompteIdentifier(final JoinPoint joinPoint, final MouvementCompteObserver observer) {
        return this.extractIdentifier(joinPoint, observer.compteIdentifierExpression());
    }
    
    private UUID extractIdentifier(final JoinPoint joinPoint, final String expression) {
        final MethodBasedEvaluationContext evaluationContext = this.createEvaluationContext(joinPoint, null);
        final UUID identifier = EXPRESSION_PARSER
                .parseExpression(expression)
                .getValue(evaluationContext, UUID.class);
        if (identifier == null) {
            throw new IllegalStateException("L'expression SpEL a renvoyé null");
        }
        return identifier;
    }

    private MethodBasedEvaluationContext createEvaluationContext(final JoinPoint joinPoint, final Object result) {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(
                joinPoint.getTarget(),
                method,
                joinPoint.getArgs(),
                PARAMETER_NAME_DISCOVERER
        );
        evaluationContext.setVariable("result", result);
        return evaluationContext;
    }
}
