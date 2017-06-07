package com.uom.las3014.aop;

import com.uom.las3014.dao.User;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthSessionTokenAopUnitTests {
    private static final String SESSION_TOKEN="SessionToken";

    @Mock
    private UserService userServiceMock;
    @InjectMocks
    private AuthSessionTokenAop authSessionTokenAop;
    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User("Username", "Password");
        user.setSessionToken(SESSION_TOKEN);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void sessionTokenBefore_userDoesNotExist_invalidCredentialsException(){
        when(userServiceMock.getUserFromDbUsingSessionToken(anyString())).thenThrow(new InvalidCredentialsException());

        authSessionTokenAop.sessionTokenBefore(SESSION_TOKEN);

        verify(userServiceMock).getUserFromDbUsingSessionToken(anyString());
    }

    @Test(expected = InvalidCredentialsException.class)
    public void sessionTokenBefore_sessionTokenNotActive_invalidCredentialsException(){
        user.setSessionTokenCreated(new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));

        when(userServiceMock.getUserFromDbUsingSessionToken(anyString())).thenReturn(user);

        authSessionTokenAop.sessionTokenBefore(SESSION_TOKEN);

        assertNull(user.getSessionToken());
        assertNull(user.getSessionTokenCreated());
        assertNull(user.getSessionTokenLastUsed());
        verify(userServiceMock).getUserFromDbUsingSessionToken(anyString());
    }

    @Test
    public void sessionTokenBefore_sessionTokenActive_sessionTokenLastUsedUpdated(){
        final Timestamp oldSessionTokenCreated = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        final Timestamp oldSessionTokenLastUsed = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));

        user.setSessionTokenCreated(oldSessionTokenCreated);
        user.setSessionTokenLastUsed(oldSessionTokenLastUsed);

        when(userServiceMock.getUserFromDbUsingSessionToken(anyString())).thenReturn(user);

        Mockito.doAnswer((InvocationOnMock invocation) -> {
                user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));
                return user;
        }).when(userServiceMock).updateSessionTokenLastUsed(user);

        authSessionTokenAop.sessionTokenBefore(SESSION_TOKEN);

        assertEquals(SESSION_TOKEN, user.getSessionToken());
        assertEquals(oldSessionTokenCreated, user.getSessionTokenCreated());
        assertTrue(user.getSessionTokenLastUsed().getTime() > oldSessionTokenLastUsed.getTime());
        verify(userServiceMock).getUserFromDbUsingSessionToken(anyString());
        verify(userServiceMock).updateSessionTokenLastUsed(user);
    }
}
