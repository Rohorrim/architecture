package com.wgt.imt.architecture.interfaces.rest.clients.model.input;

import com.wgt.imt.architecture.interfaces.rest.common.model.input.AbstractUpdateInput;
import com.wgt.imt.architecture.interfaces.rest.common.model.input.UpdatableProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

/**
 * Classe dédiée à la mise à jour d'un client via l'API REST.
 *
 * Ces attributs sont faiblement typés pour permettre une validation et un traitement ultérieur dans la couche de service.
 * De plus, ils sont encapsulés dans des propriétés modifiables pour indiquer si une mise à jour est nécessaire ou non.
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ClientUpdateInput extends AbstractUpdateInput {

    @Serial
    private static final long serialVersionUID = -6190479828349200043L;
    private UpdatableProperty<String> lastname = UpdatableProperty.empty();
    private UpdatableProperty<String> firstname = UpdatableProperty.empty();
    private UpdatableProperty<String> genre = UpdatableProperty.empty();
    
    /**
     * Permet à Jackson, lors de la désérialisation, de savoir si le champ "lastname" a été modifié ou non.
     * @param lastname : Nouvelle valeur du nom de famille. Si null, cela signifie qu'on supprime la valeur.
     */
    public void setLastname(final String lastname) {
        this.lastname = UpdatableProperty.makesChanges(lastname);
    }

    /**
     * Permet à Jackson, lors de la désérialisation, de savoir si le champ "firstname" a été modifié ou non.
     * @param firstname : Nouvelle valeur du prénom. Si null, cela signifie qu'on supprime la valeur.
     */
    public void setFirstname(final String firstname) {
        this.firstname = UpdatableProperty.makesChanges(firstname);
    }

    /**
     * Permet à Jackson, lors de la désérialisation, de savoir si le champ "genre" a été modifié ou non.
     * @param genre : Nouvelle valeur du genre. Si null, cela signifie qu'on supprime la valeur.
     */
    public void setGenre(final String genre) {
        this.genre = UpdatableProperty.makesChanges(genre);
    }
}
