package com.wgt.imt.architecture.interfaces.rest.common.model.output;

import lombok.*;

import java.io.Serial;

/**
 * Classe représentant la sortie d'une exception dans l'API REST.
 *
 * Elle encapsule les informations pertinentes sur l'exception, telles que le type, le message, le timestamp et le chemin de la requête.
 */
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ExceptionOutput extends AbstractOutput {

    @Serial
    private static final long serialVersionUID = 3305123755780216165L;
    private final String type;
    private final String message;
    private final String timestamp;
    private final String path;
}
