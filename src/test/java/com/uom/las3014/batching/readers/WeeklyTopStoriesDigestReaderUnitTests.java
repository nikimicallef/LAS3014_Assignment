package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeeklyTopStoriesDigestReaderUnitTests {
    private long DATE_TIME_EXECUTED_MILLIS = System.currentTimeMillis();

    @Mock
    private StoriesService storiesServiceMock;
    private WeeklyTopStoriesDigestReader weeklyTopStoriesDigestReader;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void read_topicsReturnsEmptylist_returnsNull() throws Exception {
        when(storiesServiceMock.getTop3UndeletedStoriesAfterTimestamp(any(Timestamp.class))).thenReturn(new ArrayList<>());
        weeklyTopStoriesDigestReader = new WeeklyTopStoriesDigestReader(storiesServiceMock);
        ReflectionTestUtils.setField(weeklyTopStoriesDigestReader, "dateTimeExecutedMillis", DATE_TIME_EXECUTED_MILLIS);

        final Story read = weeklyTopStoriesDigestReader.read();

        assertNull(read);
        verify(storiesServiceMock).getTop3UndeletedStoriesAfterTimestamp(any(Timestamp.class));
    }

    @Test
    public void read_topicReturned_returnsTopicInList() throws Exception {
        final Story exampleStory = new Story();
        when(storiesServiceMock.getTop3UndeletedStoriesAfterTimestamp(any(Timestamp.class))).thenReturn(Collections.singletonList(exampleStory));
        weeklyTopStoriesDigestReader = new WeeklyTopStoriesDigestReader(storiesServiceMock);
        ReflectionTestUtils.setField(weeklyTopStoriesDigestReader, "dateTimeExecutedMillis", DATE_TIME_EXECUTED_MILLIS);

        final Story story = weeklyTopStoriesDigestReader.read();

        assertEquals(exampleStory, story);
        verify(storiesServiceMock).getTop3UndeletedStoriesAfterTimestamp(any(Timestamp.class));
    }
}
