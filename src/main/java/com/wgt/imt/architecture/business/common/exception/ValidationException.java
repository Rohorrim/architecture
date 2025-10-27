package com.wgt.imt.architecture.business.common.exception;

import java.io.Serial;

/**
 * Classe de base des exceptions métier, sans dépendance à un protocole de présentation.
 */
public abstract class ValidationException extends RuntimeException {
    
    @Serial
    private static final long serialVersionUID = 8530100570454088203L;
    
    protected ValidationException(final String message) {
        super(message);
    }
}
