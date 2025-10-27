package com.wgt.imt.architecture.business.comptes.model;

import com.wgt.imt.architecture.business.common.model.MouvementCompteType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Événement publié après un mouvement sur un compte.
 * Contient les détails utiles au traitement et à la traçabilité du mouvement.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Mouvement {
    
    /**
     * Identifiant du mouvement
     */
    @Builder.Default
    private final UUID identifier = UUID.randomUUID();
    
    /**
     * Date du mouvement
     */
    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    /**
     * Description du mouvement effectué
     */
    private final String message;
}
