package com.wgt.imt.architecture.business.common.exception;

import java.io.Serial;

/**
 * Exception métier indiquant un conflit avec l'état actuel des données.
 */
public final class ConflictValidationException extends ValidationException {
    
    @Serial
    private static final long serialVersionUID = -2960496153666429644L;
    
    public ConflictValidationException(final String message) {
        super(message);
    }
}
