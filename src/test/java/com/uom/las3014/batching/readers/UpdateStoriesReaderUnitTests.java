package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateStoriesReaderUnitTests {
    @Mock
    private StoriesService storiesServiceMock;
    private UpdateStoriesReader updateStoriesReader;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void read_storiesToUpdateIsEmpty_returnsNull() throws Exception {
        when(storiesServiceMock.getUndeletedTopicsAfterTimestamp(any(Timestamp.class))).thenReturn(new ArrayList<>());

        updateStoriesReader = new UpdateStoriesReader(storiesServiceMock);

        final Story story = updateStoriesReader.read();

        assertNull(story);
        verify(storiesServiceMock).getUndeletedTopicsAfterTimestamp(any(Timestamp.class));
    }

    @Test
    public void read_storiesToUpdateReturnsList_returnsNewStoryId() throws Exception {
        final Story exampleStory = new Story(1L, 10, "Title", "Url", new Timestamp(System.currentTimeMillis()));
        final List<Story> storyList = Collections.singletonList(exampleStory);
        when(storiesServiceMock.getUndeletedTopicsAfterTimestamp(any(Timestamp.class))).thenReturn(storyList);

        updateStoriesReader = new UpdateStoriesReader(storiesServiceMock);

        final Story newStory = updateStoriesReader.read();

        assertEquals(exampleStory, newStory);
        verify(storiesServiceMock).getUndeletedTopicsAfterTimestamp(any(Timestamp.class));
    }
}
