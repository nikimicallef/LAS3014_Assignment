package com.uom.las3014.batching.readers;

import com.uom.las3014.httpconnection.HackernewsRequester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class GetNewStoriesReaderUnitTests {
    @Mock
    private HackernewsRequester hackernewsRequester;
    private GetNewStoriesReader getNewStoriesReader;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void read_getNewStoriesIsEmpty_returnsNull() throws Exception {
        final Optional<List<String>> optionalList = Optional.empty();
        when(hackernewsRequester.getNewStories()).thenReturn(optionalList);

        getNewStoriesReader = new GetNewStoriesReader(hackernewsRequester);

        final String newStoryId = getNewStoriesReader.read();

        assertNull(newStoryId);
        verify(hackernewsRequester).getNewStories();
    }

    @Test
    public void read_getNewStoriesReturnsList_returnsNewStoryId() throws Exception {
        final String exampleNewStoryId = "100";
        final Optional<List<String>> optionalList = Optional.of(Collections.singletonList(exampleNewStoryId));
        when(hackernewsRequester.getNewStories()).thenReturn(optionalList);

        getNewStoriesReader = new GetNewStoriesReader(hackernewsRequester);

        final String newStoryId = getNewStoriesReader.read();

        assertEquals(exampleNewStoryId, newStoryId);
        verify(hackernewsRequester).getNewStories();
    }
}
