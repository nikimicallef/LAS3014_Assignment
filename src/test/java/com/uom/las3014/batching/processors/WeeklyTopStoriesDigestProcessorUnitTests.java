package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.User;
import com.uom.las3014.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeeklyTopStoriesDigestProcessorUnitTests {
    private Long DATE_TIME_EXECUTED_MILLIS = System.currentTimeMillis();

    @Mock
    private UserService userService;
    @InjectMocks
    private WeeklyTopStoriesDigestProcessor weeklyTopStoriesDigestProcessor;
    private Story story;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(weeklyTopStoriesDigestProcessor, "dateTimeExecutedMillis", DATE_TIME_EXECUTED_MILLIS);

        final Timestamp dateCreated = new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        story = new Story(123L, 10, "Title", "Url", dateCreated);
    }

    @Test
    public void process_noUsersdigesthasNoUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        final Digest digestCreated = weeklyTopStoriesDigestProcessor.process(story);

        assertEquals(0, digestCreated.getUsersAssignedToDigest().size());
        assertEquals(story, digestCreated.getStoryId());
        verify(userService).getAllUsers();
    }

    @Test
    public void process_userPresent_digesthasNoUsers() throws Exception {
        final User userAssignedToDigest = new User("Username", "Password");
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userAssignedToDigest));

        final Digest digestCreated = weeklyTopStoriesDigestProcessor.process(story);

        assertEquals(1, digestCreated.getUsersAssignedToDigest().size());
        assertTrue(digestCreated.getUsersAssignedToDigest().contains(userAssignedToDigest));
        assertEquals(story, digestCreated.getStoryId());
        verify(userService).getAllUsers();
    }
}
