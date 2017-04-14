package com.uom.las3014.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        final Map<String, String> errorMessages = fieldErrors
//                .parallelStream()
//                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//        final Map<String, Object> errors = new HashMap<>();
//        errors.put("errors", errorMessages);
//        return constructResponse(ex, request, HttpStatus.BAD_REQUEST, errors);
//    }
//
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
//    @ExceptionHandler(IllegalArgumentException.class)
//    public void handleIllegalArgumentException() {
//        logger.error("IOException handler executed");
//    }
//
//    private ResponseEntity<Object> constructResponse(final Exception e, final WebRequest request, final HttpStatus status, final Map<String, Object> errors) {
//        final HttpHeaders errorHeaders = constructHttpHeaders();
//        return handleExceptionInternal(e, errors, errorHeaders, status, request);
//    }
//
//    private HttpHeaders constructHttpHeaders() {
//        final HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        return httpHeaders;
//    }
}
