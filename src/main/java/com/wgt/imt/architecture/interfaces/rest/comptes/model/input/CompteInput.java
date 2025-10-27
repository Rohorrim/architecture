package com.wgt.imt.architecture.interfaces.rest.comptes.model.input;

import com.wgt.imt.architecture.interfaces.rest.common.model.input.AbstractInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

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
    @Serial
    private static final long serialVersionUID = -790440597537439877L;
    private String name;
    private String type;
}
