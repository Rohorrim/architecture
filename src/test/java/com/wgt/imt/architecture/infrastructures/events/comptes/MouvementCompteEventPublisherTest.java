package com.wgt.imt.architecture.infrastructures.events.comptes;

import com.wgt.imt.architecture.business.common.model.MouvementCompteType;
import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import com.wgt.imt.architecture.business.comptes.annotations.MouvementCompteObserver;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.infrastructures.events.comptes.model.MouvementCompteEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MouvementCompteEventPublisher")
class MouvementCompteEventPublisherTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Captor
    private ArgumentCaptor<MouvementCompteEvent> eventCaptor;

    private MouvementCompteEventPublisher mouvementCompteEventPublisher;
    private UUID clientUUID;
    private UUID compteUUID;
    private Compte compte;

    @BeforeEach
    void setUp() {
        mouvementCompteEventPublisher = new MouvementCompteEventPublisher(eventPublisher);
        clientUUID = UUID.randomUUID();
        compteUUID = UUID.randomUUID();
        compte = Compte.builder()
                .identifier(compteUUID)
                .name("Compte_Test")
                .type(TypeCompteEnum.COMPTE_COURANT)
                .solde(-50.0)
                .build();
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.getTarget()).thenReturn(this);
    }

    @Nested
    @DisplayName("publierMouvement(JoinPoint, MouvementCompteObserver, Object)")
    class PublierMouvement {

        @Test
        @DisplayName("Devrait publier l'événement avec les identifiants et métadonnées de l'annotation")
        void shouldPublishEventWithIdentifiersAndAnnotationMetadata() throws NoSuchMethodException {
            // Given
            configureInvocation("observedMethod", new Object[]{clientUUID, compte});
            MouvementCompteObserver observer = observerFor("observedMethod");

            // When
            mouvementCompteEventPublisher.publierMouvement(joinPoint, observer, null);

            // Then
            verify(eventPublisher).publishEvent(eventCaptor.capture());
            MouvementCompteEvent event = eventCaptor.getValue();
            assertEquals(clientUUID, event.getClientIdentifier());
            assertEquals(compteUUID, event.getCompteIdentifier());
            assertEquals("Mouvement de test", event.getMessage());
            assertEquals(MouvementCompteType.UTILISATEUR, event.getType());
            assertSame(mouvementCompteEventPublisher, event.getSource());
        }

        @Test
        @DisplayName("Ne devrait pas publier l'événement lorsque la condition est fausse")
        void shouldNotPublishEventWhenConditionIsFalse() throws NoSuchMethodException {
            // Given
            configureInvocation("conditionalMethod", new Object[]{clientUUID, compte});
            MouvementCompteObserver observer = observerFor("conditionalMethod");

            // When
            mouvementCompteEventPublisher.publierMouvement(joinPoint, observer, false);

            // Then
            verifyNoInteractions(eventPublisher);
        }

        @Test
        @DisplayName("Devrait échouer si l'expression d'identifiant renvoie null")
        void shouldThrowWhenIdentifierExpressionReturnsNull() throws NoSuchMethodException {
            // Given
            configureInvocation("observedMethod", new Object[]{null, compte});
            MouvementCompteObserver observer = observerFor("observedMethod");

            // When & Then
            assertThrows(IllegalStateException.class,
                    () -> mouvementCompteEventPublisher.publierMouvement(joinPoint, observer, null));
            verifyNoInteractions(eventPublisher);
        }

        private void configureInvocation(String methodName, Object[] arguments) throws NoSuchMethodException {
            Method method = TestSubject.class.getDeclaredMethod(methodName, UUID.class, Compte.class);
            when(methodSignature.getMethod()).thenReturn(method);
            when(joinPoint.getArgs()).thenReturn(arguments);
        }

        private MouvementCompteObserver observerFor(String methodName) throws NoSuchMethodException {
            Method method = TestSubject.class.getDeclaredMethod(methodName, UUID.class, Compte.class);
            return method.getAnnotation(MouvementCompteObserver.class);
        }
    }

    private static class TestSubject {
        @MouvementCompteObserver(
                message = "Mouvement de test",
                type = MouvementCompteType.UTILISATEUR,
                compteIdentifierExpression = "#p1.identifier"
        )
        void observedMethod(UUID clientIdentifier, Compte compte) {
        }

        @MouvementCompteObserver(
                message = "Mouvement conditionnel",
                type = MouvementCompteType.AUTOMATIQUE,
                compteIdentifierExpression = "#p1.identifier",
                doesPublish = "#result"
        )
        void conditionalMethod(UUID clientIdentifier, Compte compte) {
        }
    }
}
