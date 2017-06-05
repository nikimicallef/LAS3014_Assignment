package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse.TopStoryResponse;
import com.uom.las3014.api.response.MultipleTopStoriesPerDateResponse;
import com.uom.las3014.dao.*;
import com.uom.las3014.dao.springdata.DigestDaoRepository;
import com.uom.las3014.exceptions.InvalidDateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DigestsServiceImplTests {
    private static final Date DIGEST_DAY_OF_WEEK = new Date(System.currentTimeMillis());
    private static final String TOPIC_NAME = "TestTopic1";
    private static final Long STORY1_ID = 123L;
    private static final Integer STORY1_SCORE = 10;
    private static final String STORY1_TITLE = "StoryTitle1";
    private static final String STORY1_URL = "www.test.com";
    private static final Long STORY2_ID = 124L;
    private static final Integer STORY2_SCORE = 15;
    private static final String STORY2_TITLE = "StoryTitle2";
    private static final String STORY2_URL = "www.test.com";
    private static final String USERNAME = "TestUser";
    private static final String PASSWORD = "TestPassword";

    @Mock
    private DigestDaoRepository digestDaoRepository;
    @InjectMocks
    private DigestsServiceImpl digestsService;
    private Topic topic;
    private Story story1;
    private Story story2;
    private User user;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        topic = new Topic(TOPIC_NAME);
        topic.setTopicId(1L);
        story1 = new Story(STORY1_ID, STORY1_SCORE, STORY1_TITLE, STORY1_URL, new Timestamp(System.currentTimeMillis()));
        story2 = new Story(STORY2_ID, STORY2_SCORE, STORY2_TITLE, STORY2_URL, new Timestamp(System.currentTimeMillis()));
        user = new User(USERNAME, PASSWORD);
    }

    @Test
    public void saveAll_noDigestsToSaved(){
        when(digestDaoRepository.save(Matchers.<Iterable<Digest>>any())).thenReturn(new ArrayList<>());

        digestsService.saveAll(new ArrayList<>());

        verify(digestDaoRepository, times(1)).save(Matchers.<Iterable<Digest>>any());
    }

    @Test
    public void saveAll_digestsSaved(){
        final List<Digest> digests = Collections.singletonList(new Digest(DIGEST_DAY_OF_WEEK, topic, story1, new HashSet<>()));

        when(digestDaoRepository.save(Matchers.<Iterable<Digest>>any())).thenReturn(digests);

        digestsService.saveAll(digests);

        verify(digestDaoRepository, times(1)).save(Matchers.<Iterable<Digest>>any());
    }

    @Test
    public void deleteDigestByDayOfWeekBefore_entriesDeleted(){
        doNothing().when(digestDaoRepository).deleteDigestByDayOfWeekBefore(any(Date.class));

        digestsService.deleteDigestByDayOfWeekBefore(new Timestamp(System.currentTimeMillis()));

        verify(digestDaoRepository, times(1)).deleteDigestByDayOfWeekBefore(any(Date.class));
    }

    @Test
    public void getLatestWeeklyDigest_noDigestsAvailable_emptyResponse(){
        when(digestDaoRepository.findLatestDigestsForUser(user)).thenReturn(new HashSet<>());

        final ResponseEntity<GroupTopStoriesByDateResponse> latestWeeklyDigest = digestsService.getLatestWeeklyDigest(user);

        assertEquals(200, latestWeeklyDigest.getStatusCodeValue());
        assertNull(latestWeeklyDigest.getBody().getEffectiveDate());
        assertEquals(0, latestWeeklyDigest.getBody().getTopics().size());
        verify(digestDaoRepository, times(1)).findLatestDigestsForUser(user);
    }

    @Test
    public void getLatestWeeklyDigest_oneStoryForWeeklyDigest_emptyResponse(){
        final Digest weeklyDigest1 = new Digest(DIGEST_DAY_OF_WEEK, null, story1, Collections.singleton(user));
        user.getDigestsAssignedToUser().add(weeklyDigest1);
        user.getUserTopics().add(new UserTopicMapping(user, topic, new Timestamp(System.currentTimeMillis())));

        when(digestDaoRepository.findLatestDigestsForUser(user)).thenReturn(user.getDigestsAssignedToUser());

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(DIGEST_DAY_OF_WEEK.toLocalDate());

        final TopStoriesForTopicResponse weeklyResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse("weekly");
        final TopStoriesForTopicResponse topicResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC_NAME);

        final TopStoryResponse weeklyTopStory = weeklyResponse.new TopStoryResponse(STORY1_TITLE, STORY1_URL, STORY1_SCORE);
        weeklyResponse.getTopStories().add(weeklyTopStory);

        groupTopStoriesByDateResponse.getTopics().add(weeklyResponse);
        groupTopStoriesByDateResponse.getTopics().add(topicResponse);

        final ResponseEntity<GroupTopStoriesByDateResponse> latestWeeklyDigest = digestsService.getLatestWeeklyDigest(user);

        assertEquals(200, latestWeeklyDigest.getStatusCodeValue());
        assertNotNull(latestWeeklyDigest.getBody().getEffectiveDate());
        assertEquals(groupTopStoriesByDateResponse, latestWeeklyDigest.getBody());
        verify(digestDaoRepository, times(1)).findLatestDigestsForUser(user);
    }

    @Test
    public void getLatestWeeklyDigest_oneStoryForTopicAndWeeklyDigest_emptyResponse(){
        final Digest weeklyDigest1 = new Digest(DIGEST_DAY_OF_WEEK, null, story1, Collections.singleton(user));
        user.getDigestsAssignedToUser().add(weeklyDigest1);
        final Digest topicDigest1 = new Digest(DIGEST_DAY_OF_WEEK, topic, story2, Collections.singleton(user));
        user.getDigestsAssignedToUser().add(topicDigest1);
        user.getUserTopics().add(new UserTopicMapping(user, topic, new Timestamp(System.currentTimeMillis())));

        when(digestDaoRepository.findLatestDigestsForUser(user)).thenReturn(user.getDigestsAssignedToUser());

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(DIGEST_DAY_OF_WEEK.toLocalDate());

        final TopStoriesForTopicResponse weeklyResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse("weekly");
        final TopStoriesForTopicResponse topicResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC_NAME);

        final TopStoryResponse weeklyTopStory = weeklyResponse.new TopStoryResponse(STORY1_TITLE, STORY1_URL, STORY1_SCORE);
        weeklyResponse.getTopStories().add(weeklyTopStory);

        final TopStoryResponse topicTopStory = topicResponse.new TopStoryResponse(STORY2_TITLE, STORY2_URL, STORY2_SCORE);
        topicResponse.getTopStories().add(topicTopStory);

        groupTopStoriesByDateResponse.getTopics().add(weeklyResponse);
        groupTopStoriesByDateResponse.getTopics().add(topicResponse);

        final ResponseEntity<GroupTopStoriesByDateResponse> latestWeeklyDigest = digestsService.getLatestWeeklyDigest(user);

        assertEquals(200, latestWeeklyDigest.getStatusCodeValue());
        assertNotNull(latestWeeklyDigest.getBody().getEffectiveDate());
        assertEquals(groupTopStoriesByDateResponse, latestWeeklyDigest.getBody());
        verify(digestDaoRepository, times(1)).findLatestDigestsForUser(user);
    }

    @Test
    public void getLatestWeeklyDigest_twoStoryForWeeklyDigest_emptyResponse(){
        final Digest weeklyDigest1 = new Digest(DIGEST_DAY_OF_WEEK, null, story1, Collections.singleton(user));
        user.getDigestsAssignedToUser().add(weeklyDigest1);
        final Digest weeklyDigest2 = new Digest(DIGEST_DAY_OF_WEEK, null, story2, Collections.singleton(user));
        user.getDigestsAssignedToUser().add(weeklyDigest2);
        user.getUserTopics().add(new UserTopicMapping(user, topic, new Timestamp(System.currentTimeMillis())));

        when(digestDaoRepository.findLatestDigestsForUser(user)).thenReturn(user.getDigestsAssignedToUser());

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(DIGEST_DAY_OF_WEEK.toLocalDate());

        final TopStoriesForTopicResponse weeklyResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse("weekly");
        final TopStoriesForTopicResponse topicResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC_NAME);

        final TopStoryResponse weeklyTopStory1 = weeklyResponse.new TopStoryResponse(STORY1_TITLE, STORY1_URL, STORY1_SCORE);
        weeklyResponse.getTopStories().add(weeklyTopStory1);

        final TopStoryResponse weeklyTopStory2 = weeklyResponse.new TopStoryResponse(STORY2_TITLE, STORY2_URL, STORY2_SCORE);
        weeklyResponse.getTopStories().add(weeklyTopStory2);

        groupTopStoriesByDateResponse.getTopics().add(weeklyResponse);
        groupTopStoriesByDateResponse.getTopics().add(topicResponse);

        final ResponseEntity<GroupTopStoriesByDateResponse> latestWeeklyDigest = digestsService.getLatestWeeklyDigest(user);

        assertEquals(200, latestWeeklyDigest.getStatusCodeValue());
        assertNotNull(latestWeeklyDigest.getBody().getEffectiveDate());
        assertEquals(groupTopStoriesByDateResponse, latestWeeklyDigest.getBody());
        verify(digestDaoRepository, times(1)).findLatestDigestsForUser(user);
    }

    @Test(expected = InvalidDateException.class)
    public void getGroupOfWeeklyDigests_dateAfterBeforeDateBefore_invalidDateException(){
        digestsService.getGroupOfWeeklyDigests(user, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
    }

    @Test
    public void getGroupOfWeeklyDigests_oneStoryForWeeklyDigest_emptyResponse(){
        final Digest weeklyDigest1 = new Digest(DIGEST_DAY_OF_WEEK, null, story1, Collections.singleton(user));
        final Digest weeklyDigest2 = new Digest(DIGEST_DAY_OF_WEEK, null, story2, Collections.singleton(user));
        user.getDigestsAssignedToUser().add(weeklyDigest1);
        user.getDigestsAssignedToUser().add(weeklyDigest2);

        final Date dateFrom = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2));
        final Date dateTo = new Date(System.currentTimeMillis());

        when(digestDaoRepository.findGroupOfDigestsBetweenDatesForUser(dateFrom, dateTo, user)).thenReturn(user.getDigestsAssignedToUser());

        final MultipleTopStoriesPerDateResponse digestsGroup = new MultipleTopStoriesPerDateResponse();

        final GroupTopStoriesByDateResponse week1Response = new GroupTopStoriesByDateResponse(DIGEST_DAY_OF_WEEK.toLocalDate());
        final TopStoriesForTopicResponse wk1WeeklyResponse = week1Response.new TopStoriesForTopicResponse("weekly");
        final TopStoryResponse weeklyTopStory1 = wk1WeeklyResponse.new TopStoryResponse(STORY1_TITLE, STORY1_URL, STORY1_SCORE);
        wk1WeeklyResponse.getTopStories().add(weeklyTopStory1);
        final TopStoryResponse weeklyTopStory2 = wk1WeeklyResponse.new TopStoryResponse(STORY2_TITLE, STORY2_URL, STORY2_SCORE);
        wk1WeeklyResponse.getTopStories().add(weeklyTopStory2);
        week1Response.getTopics().add(wk1WeeklyResponse);

        digestsGroup.getTopStoriesByDateResponses().add(week1Response);

        final ResponseEntity<MultipleTopStoriesPerDateResponse> latestWeeklyDigest = digestsService.getGroupOfWeeklyDigests(user, dateFrom, dateTo);

        assertEquals(200, latestWeeklyDigest.getStatusCodeValue());
        assertEquals(digestsGroup.getTopStoriesByDateResponses().get(0), latestWeeklyDigest.getBody().getTopStoriesByDateResponses().get(0));
        verify(digestDaoRepository, times(1)).findGroupOfDigestsBetweenDatesForUser(dateFrom, dateTo, user);
    }
}
