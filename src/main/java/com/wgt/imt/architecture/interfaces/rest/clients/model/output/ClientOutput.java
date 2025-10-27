package com.wgt.imt.architecture.interfaces.rest.clients.model.output;

import com.wgt.imt.architecture.business.clients.model.Client;
import com.wgt.imt.architecture.interfaces.rest.common.model.output.AbstractOutput;
import lombok.*;

import java.io.Serial;

/**
 * Classe dédiée à la restitution d'un client via l'API REST.
 *
 * Ces attributs sont faiblement typés pour permettre sérialiser les données exactement qu'on le souhaite nous (on ne laisse pas faire le framework).
 */
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ClientOutput extends AbstractOutput {

    /**
     * Convertit un objet Client en un objet ClientOutput.
     *
     * @param client : L'objet Client à convertir.
     * @return Un nouvel objet ClientOutput avec les valeurs fournies.
     */
    public static ClientOutput from(final Client client){
        return ClientOutput.builder()
                .identifier(client.getIdentifier().toString())
                .lastname(client.getLastname())
                .firstname(client.getFirstname())
                .genre(client.getGenre().name())
                .build();
    }

    @Serial
    private static final long serialVersionUID = -5881478654611574936L;
    private final String identifier;
    private final String lastname;
    private final String firstname;
    private final String genre;
}
