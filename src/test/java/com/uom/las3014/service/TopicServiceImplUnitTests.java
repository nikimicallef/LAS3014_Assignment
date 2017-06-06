package com.uom.las3014.service;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.springdata.TopicsDaoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TopicServiceImplUnitTests {
    private static final String TOPIC1 = "TestTopic1";
    private static final Long STORY_ID = 123L;
    private static final Integer STORY_SCORE = 10;
    private static final String STORY_TITLE = "StoryTitle";
    private static final String STORY_URL = "www.test.com";

    @Mock
    private TopicsDaoRepository topicsDaoRepository;
    @Mock
    private StoriesServiceImpl storiesService;
    @InjectMocks
    private TopicServiceImpl topicService;
    private Story story;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createNewTopicIfNotExists_topicExists_topicReturned(){
        final Story topStory = new Story(STORY_ID, STORY_SCORE, STORY_TITLE, STORY_URL, new Timestamp(System.currentTimeMillis()));

        final Topic topic = new Topic(TOPIC1);
        topic.setTopStoryId(topStory);

        when(topicsDaoRepository.findTopicsByTopicName(TOPIC1)).thenReturn(Optional.of(topic));

        final Topic newTopic = topicService.createNewTopicIfNotExists(TOPIC1);

        assertEquals(topic, newTopic);

        verify(topicsDaoRepository, times(1)).findTopicsByTopicName(TOPIC1);
    }

    @Test
    public void createNewTopicIfNotExists_topicDoesNotExistAndOneTopStory_topicCreated(){
        final Story topStory = new Story(STORY_ID, STORY_SCORE, STORY_TITLE, STORY_URL, new Timestamp(System.currentTimeMillis()));

        final Topic topic = new Topic(TOPIC1);

        when(topicsDaoRepository.findTopicsByTopicName(TOPIC1)).thenReturn(Optional.empty());
        when(topicsDaoRepository.save(any(Topic.class))).thenReturn(new Topic(TOPIC1));
        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(eq(topic.getTopicName()), any(Timestamp.class)))
                            .thenReturn(Collections.singletonList(topStory));

        final Topic newTopic = topicService.createNewTopicIfNotExists(TOPIC1);

        assertEquals(topStory, newTopic.getTopStoryId());
        verify(topicsDaoRepository, times(1)).findTopicsByTopicName(TOPIC1);
        verify(topicsDaoRepository, times(1)).save(any(Topic.class));
        verify(storiesService, times(1))
                .getUndeletedStoriesContainingKeywordAndAfterTimestamp(eq(topic.getTopicName()), any(Timestamp.class));
    }

    @Test
    public void createNewTopicIfNotExists_topicDoesNotExistAndMultipleTopStories_topicCreated(){
        final Story topStory1 = new Story(STORY_ID, STORY_SCORE, STORY_TITLE, STORY_URL, new Timestamp(System.currentTimeMillis()));
        final Story topStory2 = new Story(STORY_ID+1L, STORY_SCORE+10, STORY_TITLE, STORY_URL, new Timestamp(System.currentTimeMillis()));

        final Topic topic = new Topic(TOPIC1);

        when(topicsDaoRepository.findTopicsByTopicName(TOPIC1)).thenReturn(Optional.empty());
        when(topicsDaoRepository.save(any(Topic.class))).thenReturn(new Topic(TOPIC1));
        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(eq(topic.getTopicName()), any(Timestamp.class)))
                .thenReturn(Arrays.asList(topStory1, topStory2));

        final Topic newTopic = topicService.createNewTopicIfNotExists(TOPIC1);

        assertEquals(topStory2, newTopic.getTopStoryId());
        verify(topicsDaoRepository, times(1)).findTopicsByTopicName(TOPIC1);
        verify(topicsDaoRepository, times(1)).save(any(Topic.class));
        verify(storiesService, times(1))
                .getUndeletedStoriesContainingKeywordAndAfterTimestamp(eq(topic.getTopicName()), any(Timestamp.class));
    }

    @Test
    public void createNewTopicIfNotExists_topicDoesNotExistAndNoTopStories_topicCreated(){
        final Topic topic = new Topic(TOPIC1);

        when(topicsDaoRepository.findTopicsByTopicName(TOPIC1)).thenReturn(Optional.empty());
        when(topicsDaoRepository.save(any(Topic.class))).thenReturn(new Topic(TOPIC1));
        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(eq(topic.getTopicName()), any(Timestamp.class)))
                .thenReturn(new ArrayList<>());

        final Topic newTopic = topicService.createNewTopicIfNotExists(TOPIC1);

        assertNull(newTopic.getTopStoryId());
        verify(topicsDaoRepository, times(1)).findTopicsByTopicName(TOPIC1);
        verify(topicsDaoRepository, times(1)).save(any(Topic.class));
        verify(storiesService, times(1))
                .getUndeletedStoriesContainingKeywordAndAfterTimestamp(eq(topic.getTopicName()), any(Timestamp.class));
    }

    @Test
    public void getAllTopics_noTopics_noTopicsReturned(){
        when(topicsDaoRepository.findAll()).thenReturn(new ArrayList<>());

        final List<Topic> allTopics = topicService.getAllTopics();

        assertEquals(0, allTopics.size());
        verify(topicsDaoRepository, times(1)).findAll();
    }

    @Test
    public void getAllTopics_1TopicsExists_topicsReturned(){
        when(topicsDaoRepository.findAll()).thenReturn(Collections.singletonList(new Topic(TOPIC1)));

        final List<Topic> allTopics = topicService.getAllTopics();

        assertEquals(1, allTopics.size());
        verify(topicsDaoRepository, times(1)).findAll();
    }

    @Test
    public void saveAllTopics_noTopicsToSave(){
        when(topicsDaoRepository.save(Matchers.<Iterable<Topic>>any())).thenReturn(new ArrayList<>());

        topicService.saveAllTopics(new ArrayList<>());

        verify(topicsDaoRepository, times(1)).save(Matchers.<Iterable<Topic>>any());
    }

    @Test
    public void saveAllTopics_topicsToSave(){
        final List<Topic> topics = Collections.singletonList(new Topic(TOPIC1));

        when(topicsDaoRepository.save(Matchers.<Iterable<Topic>>any())).thenReturn(topics);

        topicService.saveAllTopics(topics);

        verify(topicsDaoRepository, times(1)).save(Matchers.<Iterable<Topic>>any());
    }
}
