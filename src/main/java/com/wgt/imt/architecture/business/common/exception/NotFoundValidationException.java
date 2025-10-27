package com.wgt.imt.architecture.business.common.exception;

import java.io.Serial;

/**
 * Exception métier indiquant qu'une ressource nécessaire est absente.
 */
public final class NotFoundValidationException extends ValidationException {
    
    @Serial
    private static final long serialVersionUID = 303805225782318423L;
    
    public NotFoundValidationException(final String message) {
        super(message);
    }
}
