package com.uom.las3014.schedule;

import com.uom.las3014.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class SessionTokenSchedulerUnitTests {
    @Mock
    private UserService userService;
    @InjectMocks
    private SessionTokenScheduler sessionTokenScheduler;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        doNothing().when(userService).invalidateInactiveSessionTokens();
    }

    @Test
    public void performInvalidateInactiveSessionTokensJob_ok() throws InterruptedException {
        sessionTokenScheduler.performInvalidateInactiveSessionTokensJob();

        verify(userService).invalidateInactiveSessionTokens();
    }
}
