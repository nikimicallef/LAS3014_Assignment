package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.StoriesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TopStoryPerTopicProcessorUnitTests {
    @Mock
    private StoriesService storiesService;
    @InjectMocks
    private TopStoryPerTopicProcessor topStoryPerTopicProcessor;
    private Topic topic;
    private Story story1;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        topic = new Topic("TopicName");
        story1 = new Story(123L, 10, "Title", "url", new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void process_notTopStoryForKeyword_topStoryForTopicNull() throws Exception {
        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class))).thenReturn(new ArrayList<>());

        final Topic newTopic = topStoryPerTopicProcessor.process(topic);

        assertNull(newTopic.getTopStoryId());
        verify(storiesService).getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class));
    }

    @Test
    public void process_oneTopStoryForKeyword_topStoryForTopicNotNull() throws Exception {
        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class))).thenReturn(Collections.singletonList(story1));

        final Topic newTopic = topStoryPerTopicProcessor.process(topic);

        assertEquals(story1, newTopic.getTopStoryId());
        verify(storiesService).getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class));
    }

    @Test
    public void process_twoTopStoriesForKeyword_topStoryForTopicHasHighestScore() throws Exception {
        final Story story2 = new Story(124L, 15, "Title2", "url", new Timestamp(System.currentTimeMillis()));

        when(storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class))).thenReturn(Arrays.asList(story1, story2));

        final Topic newTopic = topStoryPerTopicProcessor.process(topic);

        assertEquals(story2, newTopic.getTopStoryId());
        verify(storiesService).getUndeletedStoriesContainingKeywordAndAfterTimestamp(anyString(), any(Timestamp.class));
    }
}
