package com.wgt.imt.architecture.interfaces.rest.comptes.model.input;

import com.wgt.imt.architecture.interfaces.rest.common.model.input.AbstractUpdateInput;
import com.wgt.imt.architecture.interfaces.rest.common.model.input.UpdatableProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

/**
 * Classe dédiée à la mise à jour d'un compte via l'API REST.
 *
 * Ces attributs sont faiblement typés pour permettre une validation et un traitement ultérieur dans la couche de service.
 * De plus, ils sont encapsulés dans des propriétés modifiables pour indiquer si une mise à jour est nécessaire ou non.
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class CompteUpdateInput extends AbstractUpdateInput {

    @Serial
    private static final long serialVersionUID = -6069802379242271752L;
    private UpdatableProperty<String> name = UpdatableProperty.empty();
    private UpdatableProperty<String> type = UpdatableProperty.empty();
    private UpdatableProperty<Double> solde = UpdatableProperty.empty();

    /**
     * Permet à Jackson, lors de la désérialisation, de savoir si le champ "name" a été modifié ou non.
     * @param name : Nouvelle valeur du nom. Si null, cela signifie qu'on supprime la valeur.
     */
    public void setName(final String name) {
        this.name = UpdatableProperty.makesChanges(name);
    }

    /**
     * Permet à Jackson, lors de la désérialisation, de savoir si le champ "type" a été modifié ou non.
     * @param type : Nouvelle valeur du type. Si null, cela signifie qu'on supprime la valeur.
     */
    public void setType(final String type) {
        this.type = UpdatableProperty.makesChanges(type);
    }

    /**
     * Permet à Jackson, lors de la désérialisation, de savoir si le champ "solde" a été modifié ou non.
     * @param solde : Nouvelle valeur du solde. Si null, cela signifie qu'on supprime la valeur.
     */
    public void setSolde(final Double solde) {
        this.solde = UpdatableProperty.makesChanges(solde);
    }
}