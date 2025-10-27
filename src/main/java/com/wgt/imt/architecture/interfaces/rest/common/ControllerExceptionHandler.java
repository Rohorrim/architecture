package com.wgt.imt.architecture.interfaces.rest.common;

import com.wgt.imt.architecture.interfaces.rest.common.model.output.ExceptionOutput;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Cet ExceptionHandler est utilisé pour gérer les exceptions dans les contrôleurs REST.
 *
 * Il intercepte les exceptions de type AbstractRestException et les exceptions génériques, puis renvoie une réponse HTTP appropriée avec des informations sur l'erreur.
 * (On peut le considérer comme étant un mapper entre la couche business et la couche présentation spécifique REST)
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Gère les exceptions génériques.
     *
     * @param request   la requête HTTP qui a déclenché l'exception
     * @param exception l'exception levée
     * @return une ResponseEntity contenant les détails de l'exception et le statut HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionOutput> handleGenericException(final HttpServletRequest request, final Exception exception) {
        return new ResponseEntity<>(
                ExceptionOutput.builder()
                        .type(exception.getClass().getSimpleName())
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now().format(FORMATTER))
                        .path(request.getRequestURI() + Optional.ofNullable(request.getQueryString()).map(query -> "?" + query).orElse(Strings.EMPTY))
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
