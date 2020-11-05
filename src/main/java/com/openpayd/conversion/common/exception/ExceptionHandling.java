package com.openpayd.conversion.common.exception;

import com.openpayd.conversion.common.model.Response;
import com.openpayd.conversion.conversion.exception.ParameterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandling {

    private static final org.apache.commons.logging.Log log =
            org.apache.commons.logging.LogFactory.getLog(ExceptionHandling.class);

    @ExceptionHandler(RestTemplateException.class)
    public ResponseEntity<Response> handleRestException(Exception exception) {
        log.error("Rest exception occurred! " + exception.getMessage());
        return Response.error(BAD_GATEWAY).message(exception.getMessage()).build();
    }

    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<Response> handleParameterException(Exception exception) {
        log.error("Parameter exception occurred! " + exception.getMessage());
        return Response.error(BAD_REQUEST).message(exception.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleOtherExceptions(Exception exception) {
        log.error("Undefined exception occurred! " + exception.getMessage());
        return Response.error(INTERNAL_SERVER_ERROR).message("Undefined exception occured!").build();
    }
}
