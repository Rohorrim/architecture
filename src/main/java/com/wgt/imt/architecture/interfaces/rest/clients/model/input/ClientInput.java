package com.wgt.imt.architecture.interfaces.rest.clients.model.input;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.business.common.model.GenreEnum;
import com.wgt.imt.architecture.interfaces.rest.common.model.input.AbstractInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.util.UUID;

/**
 * Classe dédiée à la création d'un client via l'API REST.
 *
 * Ces attributs sont faiblement typés pour permettre une validation et un traitement ultérieur dans la couche de service.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ClientInput extends AbstractInput {
    
    /**
     * Convertit un ClientInput en un objet Client.
     *
     * @param input : L'objet ClientInput contenant les valeurs à convertir.
     * @return Un nouvel objet Client avec les valeurs fournies.
     */
    public static Client convert(final ClientInput input) {
        return Client.builder()
                .identifier(UUID.randomUUID())
                .lastname(input.getLastname())
                .firstname(input.getFirstname())
                .genre(GenreEnum.fromOrDefault(input.getGenre()))
                .build();
    }

    @Serial
    private static final long serialVersionUID = -6256916847598118362L;
    private String lastname;
    private String firstname;
    private String genre;
}
