package com.wgt.imt.architecture.interfaces.rest.common;

import com.wgt.imt.architecture.business.common.exception.ConflictValidationException;
import com.wgt.imt.architecture.business.common.exception.ConstraintValidationException;
import com.wgt.imt.architecture.business.common.exception.NotFoundValidationException;
import com.wgt.imt.architecture.business.common.exception.ValidationException;
import com.wgt.imt.architecture.interfaces.rest.common.model.output.ExceptionOutput;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    static Stream<Object[]> businessExceptions() {
        return Stream.of(
                new Object[]{new ConstraintValidationException("validation"), HttpStatus.BAD_REQUEST, "ConstraintValidationException"},
                new Object[]{new ConflictValidationException("conflict"), HttpStatus.CONFLICT, "ConflictValidationException"},
                new Object[]{new NotFoundValidationException("not found"), HttpStatus.NOT_FOUND, "NotFoundValidationException"}
        );
    }

    @ParameterizedTest
    @MethodSource("businessExceptions")
    void shouldTranslateBusinessExceptionsToRestResponses(final ValidationException exception,
                                                          final HttpStatus expectedStatus,
                                                          final String expectedType) {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/resource");

        final ResponseEntity<ExceptionOutput> response = this.handler.handleBusinessException(request, exception);

        assertEquals(expectedStatus, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedType, response.getBody().getType());
        assertEquals(exception.getMessage(), response.getBody().getMessage());
        assertEquals("/resource", response.getBody().getPath());
    }

    @Test
    void shouldTranslateUnexpectedExceptionsToInternalServerError() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/resource");

        final ResponseEntity<ExceptionOutput> response = this.handler.handleGenericException(
                request,
                new IllegalStateException("unexpected")
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("IllegalStateException", response.getBody().getType());
        assertEquals("unexpected", response.getBody().getMessage());
        assertEquals("/resource", response.getBody().getPath());
    }
}
