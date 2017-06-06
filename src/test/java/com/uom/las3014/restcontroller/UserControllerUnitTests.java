package com.uom.las3014.restcontroller;

import com.uom.las3014.api.request.UserCreateRequestBody;
import com.uom.las3014.api.request.UserLoginRequestBody;
import com.uom.las3014.api.request.UserTopicsRequestBody;
import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.api.response.SessionTokenAndMessageResponse;
import com.uom.las3014.dao.User;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UserControllerUnitTests {
    private static final String SESSION_TOKEN = "SessionToken";

    @Mock
    private UserService userServiceMock;
    @InjectMocks
    private UserController userController;
    private User user;
    private UserTopicsRequestBody userTopicsRequestBody;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User("Username", "Password");
        userTopicsRequestBody = new UserTopicsRequestBody();
        userTopicsRequestBody.setAdditions(new ArrayList<>());
        userTopicsRequestBody.setRemovals(new ArrayList<>());

        final GenericMessageResponse genericMessageResponse = new GenericMessageResponse("GenericMessage");

        final ResponseEntity<GenericMessageResponse> genericMessageResponseEntity = ResponseEntity.status(HttpStatus.OK)
                                                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                                                    .body(genericMessageResponse);

        final SessionTokenAndMessageResponse sessionTokenAndMessageResponse = new SessionTokenAndMessageResponse("Message", "SessionToken");

        final ResponseEntity<SessionTokenAndMessageResponse> sessionTokenAndMessageResponseEntity = ResponseEntity.status(HttpStatus.OK)
                                                                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                                                                    .body(sessionTokenAndMessageResponse);


        when(userServiceMock.createNewUser(any(UserCreateRequestBody.class))).thenReturn(genericMessageResponseEntity);
        when(userServiceMock.loginAndGenerateToken(any(UserLoginRequestBody.class))).thenReturn(sessionTokenAndMessageResponseEntity);
        when(userServiceMock.logout(any(User.class))).thenReturn(genericMessageResponseEntity);
        when(userServiceMock.changeInterestedTopics(any(User.class), Matchers.<List<String>>any(), Matchers.<List<String>>any())).thenReturn(genericMessageResponseEntity);
    }


    @Test
    public void createNewUser_createdOk(){
        userController.createNewUser(any(UserCreateRequestBody.class));

        verify(userServiceMock, times(1)).createNewUser(any(UserCreateRequestBody.class));
    }

    @Test
    public void loginAndGenerateToken_loginOk(){
        userController.loginAndGenerateToken(any(UserLoginRequestBody.class));

        verify(userServiceMock, times(1)).loginAndGenerateToken(any(UserLoginRequestBody.class));
    }

    @Test
    public void logoutUser_userWithTokenExists_responseOk(){
        when(userServiceMock.getUserFromDbUsingSessionToken(SESSION_TOKEN)).thenReturn(user);

        userController.logoutUser(SESSION_TOKEN);

        verify(userServiceMock, times(1)).getUserFromDbUsingSessionToken(any(String.class));
        verify(userServiceMock, times(1)).logout(any(User.class));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void logoutUser_userWithTokenNotFound_invalidCredentialsException(){
        when(userServiceMock.getUserFromDbUsingSessionToken(SESSION_TOKEN)).thenThrow(new InvalidCredentialsException());

        userController.logoutUser(SESSION_TOKEN);

        verify(userServiceMock, times(1)).getUserFromDbUsingSessionToken(any(String.class));
        verify(userServiceMock, times(0)).logout(any(User.class));
    }

    @Test
    public void changeInterestedTopics_userWithTokenExists_responseOk(){
        when(userServiceMock.getUserFromDbUsingSessionToken(SESSION_TOKEN)).thenReturn(user);

        userController.changeInterestedTopics(SESSION_TOKEN, userTopicsRequestBody);

        verify(userServiceMock, times(1)).getUserFromDbUsingSessionToken(any(String.class));
        verify(userServiceMock, times(1)).changeInterestedTopics(any(User.class), Matchers.<List<String>>any(), Matchers.<List<String>>any());
    }

    @Test(expected = InvalidCredentialsException.class)
    public void changeInterestedTopics_userWithTokenNotFound_invalidCredentialsException(){
        when(userServiceMock.getUserFromDbUsingSessionToken(SESSION_TOKEN)).thenThrow(new InvalidCredentialsException());

        userController.changeInterestedTopics(SESSION_TOKEN, userTopicsRequestBody);

        verify(userServiceMock, times(1)).getUserFromDbUsingSessionToken(any(String.class));
        verify(userServiceMock, times(0)).changeInterestedTopics(any(User.class), Matchers.<List<String>>any(), Matchers.<List<String>>any());
    }
}
