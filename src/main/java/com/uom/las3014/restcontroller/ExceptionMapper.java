package com.uom.las3014.restcontroller;

import com.uom.las3014.api.response.GenericErrorMessageResponse;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.exceptions.InvalidDateException;
import com.uom.las3014.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Gracefully returns the user an error if an error occurs whilst processing
 */
@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<GenericErrorMessageResponse> userAlreadyExists(final Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericErrorMessageResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GenericErrorMessageResponse> invalidCredentials(final Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericErrorMessageResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<GenericErrorMessageResponse> invalidDate(final Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericErrorMessageResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorMessageResponse> catchAll(final Exception exception) {
        exception.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericErrorMessageResponse("Internal server error."));
    }
}
