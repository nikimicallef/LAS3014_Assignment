package com.uom.las3014.batching.readers.generic;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.TopicService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAllTopicsReaderUnitTests {
    @Mock
    private TopicService topicService;
    private GetAllTopicsReader getAllTopicsReader;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void read_topicsReturnsEmptylist_returnsNull() throws Exception {
        when(topicService.getAllTopics()).thenReturn(new ArrayList<>());
        getAllTopicsReader = new GetAllTopicsReader(topicService);

        final Topic read = getAllTopicsReader.read();

        assertNull(read);
        verify(topicService).getAllTopics();
    }

    @Test
    public void read_topicReturned_returnsTopicInList() throws Exception {
        final Topic topic = new Topic("TopicName");
        when(topicService.getAllTopics()).thenReturn(Collections.singletonList(topic));
        getAllTopicsReader = new GetAllTopicsReader(topicService);

        final Topic read = getAllTopicsReader.read();

        assertEquals(topic, read);
        verify(topicService).getAllTopics();
    }
}
