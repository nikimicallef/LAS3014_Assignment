package com.uom.las3014.batching.processors;

import com.google.gson.JsonObject;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetNewStoriesProcessorUnitTests {
    private static final String STORY_ID = "123";
    private static final Integer STORY_SCORE = 10;
    private static final String STORY_TITLE = "Title1";
    private static final String STORY_URL = "www.test.com";
    private static final Timestamp STORY_TIME = new Timestamp((System.currentTimeMillis())/1000);

    @Mock
    private HackernewsRequester hackernewsRequester;
    @InjectMocks
    private GetNewStoriesProcessor getNewStoriesProcessor;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void process_getItemReturnsEmpty_nullStory() throws Exception {
        when(hackernewsRequester.getItem(anyLong())).thenReturn(Optional.empty());

        final Story newStory = getNewStoriesProcessor.process(STORY_ID);

        assertNull(newStory);
        verify(hackernewsRequester).getItem(anyLong());
    }

    @Test
    public void process_getItemReturnsJsonObjectWithoutUrl_storyWithNoUrl() throws Exception {
        final JsonObject storyReturned = new JsonObject();
        storyReturned.addProperty("id", STORY_ID);
        storyReturned.addProperty("score", STORY_SCORE);
        storyReturned.addProperty("title", STORY_TITLE);
        storyReturned.addProperty("time", STORY_TIME.getTime());

        when(hackernewsRequester.getItem(anyLong())).thenReturn(Optional.of(storyReturned));

        final Story newStory = getNewStoriesProcessor.process(STORY_ID);

        assertNotNull(newStory);
        assertEquals((Long) Long.parseLong(STORY_ID), newStory.getStoryId());
        assertEquals(STORY_SCORE, newStory.getScore());
        assertEquals(STORY_TITLE, newStory.getTitle());
        assertEquals(STORY_TIME.getTime()*1000, newStory.getDateCreated().getTime());
        assertEquals("", newStory.getUrl());
        verify(hackernewsRequester).getItem(anyLong());
    }

    @Test
    public void process_getItemReturnsJsonObjectWithUrl_storyWithUrl() throws Exception {
        final JsonObject storyReturned = new JsonObject();
        storyReturned.addProperty("id", STORY_ID);
        storyReturned.addProperty("score", STORY_SCORE);
        storyReturned.addProperty("title", STORY_TITLE);
        storyReturned.addProperty("url", STORY_URL);
        storyReturned.addProperty("time", STORY_TIME.getTime());

        when(hackernewsRequester.getItem(anyLong())).thenReturn(Optional.of(storyReturned));

        final Story newStory = getNewStoriesProcessor.process(STORY_ID);

        assertNotNull(newStory);
        assertEquals((Long) Long.parseLong(STORY_ID), newStory.getStoryId());
        assertEquals(STORY_SCORE, newStory.getScore());
        assertEquals(STORY_TITLE, newStory.getTitle());
        assertEquals(STORY_TIME.getTime()*1000, newStory.getDateCreated().getTime());
        assertEquals(STORY_URL, newStory.getUrl());
        verify(hackernewsRequester).getItem(anyLong());
    }
}
