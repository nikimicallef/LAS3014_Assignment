package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.*;
import com.uom.las3014.service.StoriesService;
import com.uom.las3014.service.UserTopicMappingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeeklyTopStoriesDigestPerTopicProcessorUnitTests {
    private static final Long DATE_TIME_EXECUTED_MILLIS = System.currentTimeMillis();
    @Mock
    private StoriesService storiesService;
    @Mock
    private UserTopicMappingService userTopicMappingService;
    @InjectMocks
    private WeeklyTopStoriesDigestPerTopicProcessor weeklyTopStoriesDigestPerTopicProcessor;
    private Topic topic;
    private Story story1;
    private User user1;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(weeklyTopStoriesDigestPerTopicProcessor, "dateTimeExecutedMillis", DATE_TIME_EXECUTED_MILLIS);

        topic = new Topic("Topic1");
        story1 = new Story(123L, 10, "TestTitle1", "url", new Timestamp(System.currentTimeMillis()));
        user1 = new User("Username1", "Password1");
    }

    @Test
    public void process_noStoriesAndNoUsers_digestCreatedWithNoStoryAndNoUsers() throws Exception {
        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class))).thenReturn(new ArrayList<>());
        when(userTopicMappingService.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class))).thenReturn(new ArrayList<>());

        final Digest expectedDigest = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, null, new HashSet<>());

        final List<Digest> digests = weeklyTopStoriesDigestPerTopicProcessor.process(topic);

        assertEquals(1, digests.size());
        assertEquals(expectedDigest, digests.get(0));
        verify(storiesService).getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class));
        verify(userTopicMappingService).findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class));
    }

    @Test
    public void process_1StoriesButNoUsers_digestCreatedWithStoryButNoUsers() throws Exception {
        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class))).thenReturn(Collections.singletonList(story1));
        when(userTopicMappingService.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class))).thenReturn(new ArrayList<>());

        final Digest expectedDigest = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, story1, new HashSet<>());

        final List<Digest> digests = weeklyTopStoriesDigestPerTopicProcessor.process(topic);

        assertEquals(1, digests.size());
        assertEquals(expectedDigest, digests.get(0));
        verify(storiesService).getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class));
        verify(userTopicMappingService).findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class));
    }


    @Test
    public void process_multipleStoriesButNoUsers_digestCreatedWithTopStoriesButNoUsers() throws Exception {
        final Story story2 = new Story(124L, 15, "TestTitle2", "url", new Timestamp(System.currentTimeMillis()));
        final Story story3 = new Story(125L, 20, "TestTitle3", "url", new Timestamp(System.currentTimeMillis()));
        final Story story4 = new Story(126L, 25, "TestTitle4", "url", new Timestamp(System.currentTimeMillis()));
        final Story story5 = new Story(127L, 30, "TestTitle5", "url", new Timestamp(System.currentTimeMillis()));

        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class))).thenReturn(Arrays.asList(story1, story2, story3, story4, story5));
        when(userTopicMappingService.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class))).thenReturn(new ArrayList<>());

        final Digest expectedDigest1 = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, story5, new HashSet<>());
        final Digest expectedDigest2 = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, story4, new HashSet<>());
        final Digest expectedDigest3 = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, story3, new HashSet<>());

        final List<Digest> digests = weeklyTopStoriesDigestPerTopicProcessor.process(topic);

        assertEquals(3, digests.size());
        assertEquals(expectedDigest1, digests.get(0));
        assertEquals(expectedDigest2, digests.get(1));
        assertEquals(expectedDigest3, digests.get(2));
        verify(storiesService).getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class));
        verify(userTopicMappingService).findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class));
    }

    @Test
    public void process_multipleStoriesWithUser_digestCreatedWithTopStoriesAndUser() throws Exception {
        final Story story2 = new Story(124L, 15, "TestTitle2", "url", new Timestamp(System.currentTimeMillis()));
        final Story story3 = new Story(125L, 20, "TestTitle3", "url", new Timestamp(System.currentTimeMillis()));
        final Story story4 = new Story(126L, 25, "TestTitle4", "url", new Timestamp(System.currentTimeMillis()));
        final Story story5 = new Story(127L, 30, "TestTitle5", "url", new Timestamp(System.currentTimeMillis()));

        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class))).thenReturn(Arrays.asList(story1, story2, story3, story4, story5));

        final User user2 = new User("Username2", "Password2");

        final UserTopicMapping userTopicMapping1 = new UserTopicMapping(user1, topic, new Timestamp(System.currentTimeMillis()));
        final UserTopicMapping userTopicMapping2 = new UserTopicMapping(user2, topic, new Timestamp(System.currentTimeMillis()));

        when(userTopicMappingService.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class))).thenReturn(Arrays.asList(userTopicMapping1, userTopicMapping2));

        final Digest expectedDigest1 = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, story5, new HashSet<>(Arrays.asList(user1, user2)));
        final Digest expectedDigest2 = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, story4, new HashSet<>(Arrays.asList(user1, user2)));
        final Digest expectedDigest3 = new Digest(new Date(DATE_TIME_EXECUTED_MILLIS), topic, story3, new HashSet<>(Arrays.asList(user1, user2)));

        final List<Digest> digests = weeklyTopStoriesDigestPerTopicProcessor.process(topic);

        assertEquals(3, digests.size());
        assertEquals(expectedDigest1, digests.get(0));
        assertEquals(expectedDigest2, digests.get(1));
        assertEquals(expectedDigest3, digests.get(2));
        verify(storiesService).getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class));
        verify(userTopicMappingService).findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class));
    }
}
