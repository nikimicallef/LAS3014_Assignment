package com.uom.las3014.restcontroller;

import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.exceptions.UserAlreadyExistsException;
import com.uom.las3014.resources.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity userAlreadyExists() {
        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("error", "User already exists.");

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity invalidCredentials() {
        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("error", "Invalid Credentials.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity catchAll(final Exception exception) {
        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("error", "Internal server error.");

        logger.error(exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
    }

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
