package com.uom.las3014.restcontroller;

import com.uom.las3014.api.response.GenericErrorMessageResponse;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.exceptions.InvalidDateException;
import com.uom.las3014.exceptions.UserAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ExceptionMapperUnitTests {
    private ExceptionMapper exceptionMapper;

    @Before
    public void setUp(){
        exceptionMapper = new ExceptionMapper();
    }

    @Test
    public void userAlreadyExists_conflict(){
        final GenericErrorMessageResponse genericMessageResponse = new GenericErrorMessageResponse("User already exists");
        final ResponseEntity<GenericErrorMessageResponse> genericErrorMessageResponseEntity = exceptionMapper
                                                                .userAlreadyExists(new UserAlreadyExistsException("User already exists"));

        assertEquals(HttpStatus.CONFLICT, genericErrorMessageResponseEntity.getStatusCode());
        assertEquals(genericMessageResponse, genericErrorMessageResponseEntity.getBody());
    }

    @Test
    public void invalidCredentials_unauthorized(){
        final GenericErrorMessageResponse genericMessageResponse = new GenericErrorMessageResponse("Invalid Credentials");
        final ResponseEntity<GenericErrorMessageResponse> genericErrorMessageResponseEntity = exceptionMapper
                                                                .invalidCredentials(new InvalidCredentialsException("Invalid Credentials"));

        assertEquals(HttpStatus.UNAUTHORIZED, genericErrorMessageResponseEntity.getStatusCode());
        assertEquals(genericMessageResponse, genericErrorMessageResponseEntity.getBody());
    }

    @Test
    public void invalidDate_badRequest(){
        final GenericErrorMessageResponse genericMessageResponse = new GenericErrorMessageResponse("Invalid Date");
        final ResponseEntity<GenericErrorMessageResponse> genericErrorMessageResponseEntity = exceptionMapper
                                                                .invalidDate(new InvalidDateException("Invalid Date"));

        assertEquals(HttpStatus.BAD_REQUEST, genericErrorMessageResponseEntity.getStatusCode());
        assertEquals(genericMessageResponse, genericErrorMessageResponseEntity.getBody());
    }

    @Test
    public void catchAll_badRequest(){
        final GenericErrorMessageResponse genericMessageResponse = new GenericErrorMessageResponse("Internal server error.");
        final ResponseEntity<GenericErrorMessageResponse> genericErrorMessageResponseEntity = exceptionMapper.catchAll(new Exception());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, genericErrorMessageResponseEntity.getStatusCode());
        assertEquals(genericMessageResponse, genericErrorMessageResponseEntity.getBody());
    }
}
