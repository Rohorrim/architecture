package com.wgt.imt.architecture.business.common.exception;

import java.io.Serial;

/**
 * Exception métier indiquant que les données ne respectent pas les règles de validation.
 */
public final class ConstraintValidationException extends ValidationException {
    
    @Serial
    private static final long serialVersionUID = -9037056432067360833L;
    
    public ConstraintValidationException(final String message) {
        super(message);
    }
}
