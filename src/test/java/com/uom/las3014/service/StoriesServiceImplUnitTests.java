package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse.TopStoryResponse;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.StoriesDaoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class StoriesServiceImplUnitTests {
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
    private static final String TOPIC1_NAME = "TestTopic1";
    private static final String TOPIC2_NAME = "TestTopic2";

    @Mock
    private StoriesDaoRepository storiesDaoRepository;
    @InjectMocks
    private StoriesServiceImpl storiesService;
    private Story story1;
    private Story story2;
    private User user;
    private Topic topic1;
    private Topic topic2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        story1 = new Story(STORY1_ID, STORY1_SCORE, STORY1_TITLE, STORY1_URL, new Timestamp(System.currentTimeMillis()));
        story2 = new Story(STORY2_ID, STORY2_SCORE, STORY2_TITLE, STORY2_URL, new Timestamp(System.currentTimeMillis()));
        user = new User(USERNAME, PASSWORD);
    }

    @Test
    public void getUndeletedTopicsAfterTimestamp_noStories_emptyList(){
        when(storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalse(any(Timestamp.class)))
                .thenReturn(new ArrayList<>());

        final Timestamp createdAfter = new Timestamp(System.currentTimeMillis());
        final List<Story> stories = storiesService.getUndeletedTopicsAfterTimestamp(createdAfter);

        assertEquals(0, stories.size());
        verify(storiesDaoRepository).findAllByDateCreatedIsAfterAndDeletedIsFalse(any(Timestamp.class));
    }

    @Test
    public void getUndeletedTopicsAfterTimestamp_storiesExist_entriesInList(){
        when(storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalse(any(Timestamp.class)))
                .thenReturn(Collections.singletonList(story1));

        final Timestamp createdAfter = new Timestamp(System.currentTimeMillis());
        final List<Story> stories = storiesService.getUndeletedTopicsAfterTimestamp(createdAfter);

        assertEquals(1, stories.size());
        assertEquals(story1, stories.get(0));
        verify(storiesDaoRepository).findAllByDateCreatedIsAfterAndDeletedIsFalse(any(Timestamp.class));
    }

    @Test
    public void getUndeletedStoriesContainingKeywordAndAfterTimestamp_noStories_emptyList(){
        when(storiesDaoRepository.findAllByTitleContainingAndDateCreatedIsAfterAndDeletedIsFalse(anyString(), any(Timestamp.class)))
                .thenReturn(new ArrayList<>());

        final Timestamp createdAfter = new Timestamp(System.currentTimeMillis());
        final List<Story> stories = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(STORY1_TITLE, createdAfter);

        assertEquals(0, stories.size());
        verify(storiesDaoRepository).findAllByTitleContainingAndDateCreatedIsAfterAndDeletedIsFalse(anyString(), any(Timestamp.class));
    }

    @Test
    public void getUndeletedStoriesContainingKeywordAndAfterTimestamp_storiesExist_entriesInList(){
        when(storiesDaoRepository.findAllByTitleContainingAndDateCreatedIsAfterAndDeletedIsFalse(anyString(), any(Timestamp.class)))
                .thenReturn(Collections.singletonList(story1));

        final Timestamp createdAfter = new Timestamp(System.currentTimeMillis());
        final List<Story> stories = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(STORY1_TITLE, createdAfter);

        assertEquals(1, stories.size());
        assertEquals(story1, stories.get(0));
        verify(storiesDaoRepository).findAllByTitleContainingAndDateCreatedIsAfterAndDeletedIsFalse(anyString(), any(Timestamp.class));
    }

    @Test
    public void getTopStoryForTopics_noTopicsInterested_emptyResponse(){
        final ResponseEntity<GroupTopStoriesByDateResponse> topStoryForTopics = storiesService.getTopStoryForTopics(user);

        assertEquals(200, topStoryForTopics.getStatusCodeValue());
        assertNotNull(topStoryForTopics.getBody().getEffectiveDate());
        assertEquals(0, topStoryForTopics.getBody().getTopics().size());
    }

    @Test
    public void getTopStoryForTopics_twoInterestedTopicsWithNoTopStory_responseForTopicsButWithoutStories(){
        topic1 = new Topic(TOPIC1_NAME);
        topic2 = new Topic(TOPIC2_NAME);

        final Timestamp interestedFrom = new Timestamp(System.currentTimeMillis());
        final UserTopicMapping userTopicMapping1 = new UserTopicMapping(user, topic1, interestedFrom);
        final UserTopicMapping userTopicMapping2 = new UserTopicMapping(user, topic2, interestedFrom);

        user.getUserTopics().add(userTopicMapping1);
        user.getUserTopics().add(userTopicMapping2);

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(LocalDate.now());

        final TopStoriesForTopicResponse topic1Response = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC1_NAME);
        final TopStoriesForTopicResponse topic2Response = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC2_NAME);

        groupTopStoriesByDateResponse.getTopics().add(topic1Response);
        groupTopStoriesByDateResponse.getTopics().add(topic2Response);

        final ResponseEntity<GroupTopStoriesByDateResponse> topStoryForTopics = storiesService.getTopStoryForTopics(user);

        assertEquals(200, topStoryForTopics.getStatusCodeValue());
        assertNotNull(topStoryForTopics.getBody().getEffectiveDate());
        assertEquals(new HashSet<>(groupTopStoriesByDateResponse.getTopics()), new HashSet<>(topStoryForTopics.getBody().getTopics()));
    }

    @Test
    public void getTopStoryForTopics_twoInterestedTopicsWithTopStories_completeResponse(){
        topic1 = new Topic(TOPIC1_NAME);
        topic1.setTopStoryId(story1);
        topic2 = new Topic(TOPIC2_NAME);
        topic2.setTopStoryId(story2);

        final Timestamp interestedFrom = new Timestamp(System.currentTimeMillis());
        final UserTopicMapping userTopicMapping1 = new UserTopicMapping(user, topic1, interestedFrom);
        final UserTopicMapping userTopicMapping2 = new UserTopicMapping(user, topic2, interestedFrom);

        user.getUserTopics().add(userTopicMapping1);
        user.getUserTopics().add(userTopicMapping2);

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(LocalDate.now());

        final TopStoriesForTopicResponse topic1Response = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC1_NAME);
        final TopStoriesForTopicResponse topic2Response = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC2_NAME);

        final String title1 = topic1.getTopStoryId().getTitle();
        final String url1 = topic1.getTopStoryId().getUrl();
        final Integer score1 = topic1.getTopStoryId().getScore();
        final TopStoryResponse topic1StoryResponse = topic1Response.new TopStoryResponse(title1, url1, score1);
        topic1Response.getTopStories().add(topic1StoryResponse);

        final String title2 = topic2.getTopStoryId().getTitle();
        final String url2 = topic2.getTopStoryId().getUrl();
        final Integer score2 = topic2.getTopStoryId().getScore();
        final TopStoryResponse topic2StoryResponse = topic2Response.new TopStoryResponse(title2, url2, score2);
        topic2Response.getTopStories().add(topic2StoryResponse);

        groupTopStoriesByDateResponse.getTopics().add(topic1Response);
        groupTopStoriesByDateResponse.getTopics().add(topic2Response);

        final ResponseEntity<GroupTopStoriesByDateResponse> topStoryForTopics = storiesService.getTopStoryForTopics(user);

        assertEquals(200, topStoryForTopics.getStatusCodeValue());
        assertNotNull(topStoryForTopics.getBody().getEffectiveDate());
        assertEquals(new HashSet<>(groupTopStoriesByDateResponse.getTopics()), new HashSet<>(topStoryForTopics.getBody().getTopics()));
    }

    @Test
    public void getTopStoryForTopics_oneInterestedTopicWithTopStoriesAndIneDisabledInterestedTopic_completeResponse(){
        topic1 = new Topic(TOPIC1_NAME);
        topic1.setTopStoryId(story1);
        topic2 = new Topic(TOPIC2_NAME);
        topic2.setTopStoryId(story2);

        final UserTopicMapping userTopicMapping1 = new UserTopicMapping(user, topic1, new Timestamp(System.currentTimeMillis()));
        final UserTopicMapping userTopicMapping2 = new UserTopicMapping(user, topic2, new Timestamp(System.currentTimeMillis()));
        userTopicMapping2.setEnabled(false);

        user.getUserTopics().add(userTopicMapping1);
        user.getUserTopics().add(userTopicMapping2);

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(LocalDate.now());

        final TopStoriesForTopicResponse topic1Response = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(TOPIC1_NAME);

        final String title1 = topic1.getTopStoryId().getTitle();
        final String url1 = topic1.getTopStoryId().getUrl();
        final Integer score1 = topic1.getTopStoryId().getScore();
        final TopStoryResponse topic1StoryResponse = topic1Response.new TopStoryResponse(title1, url1, score1);
        topic1Response.getTopStories().add(topic1StoryResponse);

        groupTopStoriesByDateResponse.getTopics().add(topic1Response);

        final ResponseEntity<GroupTopStoriesByDateResponse> topStoryForTopics = storiesService.getTopStoryForTopics(user);

        assertEquals(200, topStoryForTopics.getStatusCodeValue());
        assertNotNull(topStoryForTopics.getBody().getEffectiveDate());
        assertEquals(groupTopStoriesByDateResponse.getTopics(), topStoryForTopics.getBody().getTopics());
    }

    @Test
    public void saveAllStories_noStoriesToSave(){
        when(storiesDaoRepository.save(Matchers.<Iterable<Story>>any())).thenReturn(new ArrayList<>());

        storiesService.saveAllStories(new ArrayList<>());

        verify(storiesDaoRepository).save(Matchers.<Iterable<Story>>any());
    }

    @Test
    public void saveAllStories_storiesToSave(){
        final List<Story> stories = Collections.singletonList(story1);

        when(storiesDaoRepository.save(Matchers.<Iterable<Story>>any())).thenReturn(stories);

        storiesService.saveAllStories(stories);

        verify(storiesDaoRepository).save(Matchers.<Iterable<Story>>any());
    }

    @Test
    public void getTop3UndeletedStoriesAfterTimestamp_noStories_noStoriesReturned(){
        when(storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class)))
            .thenReturn(new ArrayList<>());

        final Timestamp dateAfter = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        final List<Story> returedStories = storiesService.getTop3UndeletedStoriesAfterTimestamp(dateAfter);

        assertEquals(0, returedStories.size());
        verify(storiesDaoRepository).findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class));
    }

    @Test
    public void getTop3UndeletedStoriesAfterTimestamp_1Story_1StoryReturned(){
        when(storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class)))
                .thenReturn(Collections.singletonList(story1));

        final Timestamp dateAfter = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        final List<Story> returnedStories = storiesService.getTop3UndeletedStoriesAfterTimestamp(dateAfter);

        assertEquals(1, returnedStories.size());
        assertEquals(story1, returnedStories.get(0));
        verify(storiesDaoRepository).findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class));
    }

    @Test
    public void getTop3UndeletedStoriesAfterTimestamp_3Stories_3OrderedStoriesReturned(){
        final Story story3 = new Story(1L, 20, "Title3", "www.url3.com", new Timestamp(System.currentTimeMillis()));

        when(storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class)))
                .thenReturn(Arrays.asList(story1, story2, story3));

        final Timestamp dateAfter = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        final List<Story> returnedStories = storiesService.getTop3UndeletedStoriesAfterTimestamp(dateAfter);

        assertEquals(3, returnedStories.size());
        assertEquals(story3, returnedStories.get(0));
        assertEquals(story2, returnedStories.get(1));
        assertEquals(story1, returnedStories.get(2));
        assertTrue(returnedStories.get(0).getScore() >= returnedStories.get(1).getScore()
                && returnedStories.get(1).getScore() >= returnedStories.get(2).getScore());
        verify(storiesDaoRepository).findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class));
    }

    @Test
    public void getTop3UndeletedStoriesAfterTimestamp_5Stories_3OrderedStoriesReturned(){
        final Story story3 = new Story(1L, 20, "Title3", "www.url3.com", new Timestamp(System.currentTimeMillis()));
        final Story story4 = new Story(2L, 15, "Title4", "www.url4.com", new Timestamp(System.currentTimeMillis()));
        final Story story5 = new Story(3L, 7, "Title5", "www.url5.com", new Timestamp(System.currentTimeMillis()));

        when(storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class)))
                .thenReturn(Arrays.asList(story1, story2, story3, story4, story5));

        final Timestamp dateAfter = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        final List<Story> returnedStories = storiesService.getTop3UndeletedStoriesAfterTimestamp(dateAfter);

        assertEquals(3, returnedStories.size());
        assertEquals(story3, returnedStories.get(0));
        assertEquals(story2, returnedStories.get(1));
        assertEquals(story4, returnedStories.get(2));
        assertTrue(returnedStories.get(0).getScore() >= returnedStories.get(1).getScore()
                && returnedStories.get(1).getScore() >= returnedStories.get(2).getScore());
        verify(storiesDaoRepository).findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(any(Timestamp.class), any(Integer.class));
    }

    @Test
    public void deleteByDateCreatedBeforeAndDigestsEmpty_entriesDeleted(){
        doNothing().when(storiesDaoRepository).deleteByDateCreatedBeforeAndDigestsEmpty(any(Timestamp.class));

        storiesService.deleteByDateCreatedBeforeAndDigestsEmpty(new Timestamp(System.currentTimeMillis()));

        verify(storiesDaoRepository).deleteByDateCreatedBeforeAndDigestsEmpty(any(Timestamp.class));
    }
}
