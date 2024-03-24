package org.otis.playground.quarkus.reactive.exception;

import io.smallrye.mutiny.CompositeException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.otis.playground.quarkus.reactive.model.dto.ErrorMessageDTO;
import org.otis.playground.quarkus.reactive.model.dto.ResponseDTO;

import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static org.otis.playground.quarkus.reactive.preference.ConstantPreference.*;

@Provider
public class ErrorMapper implements ExceptionMapper<Exception> {
    private static final Logger LOGGER = Logger.getLogger(ErrorMapper.class.getName());

    @Override
    public Response toResponse(Exception exception) {
        int code = INTERNAL_SERVER_ERROR.code();

        if (null != exception.getCause() && exception.getCause() instanceof ConstraintViolationException) {
            LOGGER.info("ConstraintViolationException invoked !!!");

            code = BAD_REQUEST.code();

            List<ErrorMessageDTO> errorMessages = ((ConstraintViolationException) exception.getCause()).getConstraintViolations().stream()
                    .map(constraintViolation -> ErrorMessageDTO.builder().path(constraintViolation.getPropertyPath().toString()).message(constraintViolation.getMessage()).build())
                    .toList();

            return Response.status(code)
                    .entity(generateResponseObject(code, RESPONSE_MESSAGE_BAD_REQUEST, exception, errorMessages))
                    .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .build();
        } else if (exception instanceof WebApplicationException) {
            LOGGER.info("WebApplicationException invoked !!!");

            if (exception instanceof NotFoundException) {
                LOGGER.info("WebApplicationException - NotFoundException invoked !!!");

                code = NOT_FOUND.code();

                return Response.status(code)
                        .entity(generateResponseObject(code, RESPONSE_MESSAGE_NOT_FOUND, exception, null))
                        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .build();
            } else if (exception instanceof NotAllowedException) {
                LOGGER.info("WebApplicationException - NotAllowedException invoked !!!");

                code = METHOD_NOT_ALLOWED.code();

                return Response.status(code)
                        .entity(generateResponseObject(code, RESPONSE_MESSAGE_NOT_ALLOWED, exception, null))
                        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .build();
            }

            code = ((WebApplicationException) exception).getResponse().getStatus();
        } else if (exception instanceof CompositeException) { // This is a Mutiny exception and it happens
            LOGGER.info("CompositeException invoked !!!");
        }

        return Response.status(code)
                .entity(generateResponseObject(code, RESPONSE_MESSAGE_INTERNAL_SERVER_ERROR, exception, null))
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();
    }

    private ResponseDTO generateResponseObject(int code, String message, Exception exception, List<ErrorMessageDTO> errorMessages) {
        return ResponseDTO.builder()
                .status(String.valueOf(code))
                .message(message)
                .type(exception.getClass().getName())
                .error(null != errorMessages ? null : exception.getMessage())
                .errors(errorMessages)
                .build();
    }
}