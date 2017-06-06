package com.uom.las3014.batching.writers;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.TopicService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;

public class TopStoryPerTopicWriterUnitTests {
    @Mock
    private TopicService topicService;
    @InjectMocks
    private TopStoryPerTopicWriter topStoryPerTopicWriter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        doNothing().when(topicService).saveAllTopics(Matchers.<Iterable<Topic>>any());
    }

    @Test
    public void write_emptyList() throws Exception {
        topStoryPerTopicWriter.write(new ArrayList<>());

        verify(topicService).saveAllTopics(Matchers.<List<Topic>>any());
    }

    @Test
    public void write_listPopulated() throws Exception {
        topStoryPerTopicWriter.write(Collections.singletonList(new Topic()));

        verify(topicService).saveAllTopics(Matchers.<List<Topic>>any());
    }
}
