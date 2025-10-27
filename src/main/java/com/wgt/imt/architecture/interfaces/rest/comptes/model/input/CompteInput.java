package com.wgt.imt.architecture.interfaces.rest.comptes.model.input;

import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.interfaces.rest.common.model.input.AbstractInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.util.UUID;

/**
 * Classe dédiée à la création d'un compte via l'API REST.
 *
 * Ces attributs sont faiblement typés pour permettre une validation et un traitement ultérieur dans la couche de service.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class CompteInput extends AbstractInput {

    /**
     * Convertit un CompteInput en un objet Compte.
     *
     * @param compteInput : L'objet CompteInput contenant les valeurs à convertir.
     * @return Un nouvel objet Compte avec les valeurs fournies.
     */
    public static Compte convert(final CompteInput compteInput) {
        return Compte.builder()
                .identifier(UUID.randomUUID())
                .name(compteInput.getName())
                .type(TypeCompteEnum.fromOrDefault(compteInput.getType()))
                .solde(0.0)
                .build();
    }

    @Serial
    private static final long serialVersionUID = -790440597537439877L;
    private String name;
    private String type;
}
