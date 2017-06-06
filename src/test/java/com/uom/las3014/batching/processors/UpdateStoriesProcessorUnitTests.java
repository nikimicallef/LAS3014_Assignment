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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateStoriesProcessorUnitTests {
    private static final Integer INITIAL_SCORE = 10;
    @Mock
    private HackernewsRequester hackernewsRequester;
    @InjectMocks
    private UpdateStoriesProcessor updateStoriesProcessor;
    private Story story;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        story = new Story(123L, INITIAL_SCORE, "Title", "URL", new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void process_hackernewsReturnsNoItem_storyNull() throws Exception {
        when(hackernewsRequester.getItem(anyLong())).thenReturn(Optional.empty());

        final Story updatedStory = updateStoriesProcessor.process(story);

        assertEquals(story, updatedStory);
        verify(hackernewsRequester).getItem(anyLong());
    }

    @Test
    public void process_itemDeleted_storyDeletedFlagTrue() throws Exception {
        final JsonObject hackernewsStory = new JsonObject();
        hackernewsStory.addProperty("deleted", true);

        when(hackernewsRequester.getItem(anyLong())).thenReturn(Optional.of(hackernewsStory));

        final Story updatedStory = updateStoriesProcessor.process(story);

        assertTrue(updatedStory.isDeleted());
        verify(hackernewsRequester).getItem(anyLong());
    }

    @Test
    public void process_storyReturnedHasEqualScore_storyNotUpdated() throws Exception {
        final JsonObject hackernewsStory = new JsonObject();
        hackernewsStory.addProperty("score", INITIAL_SCORE);

        when(hackernewsRequester.getItem(anyLong())).thenReturn(Optional.of(hackernewsStory));

        final Story updatedStory = updateStoriesProcessor.process(story);

        assertEquals(INITIAL_SCORE, updatedStory.getScore());
        verify(hackernewsRequester).getItem(anyLong());
    }

    @Test
    public void process_storyReturnedHasGreaterScore_storyUpdated() throws Exception {
        final JsonObject hackernewsStory = new JsonObject();
        hackernewsStory.addProperty("score", INITIAL_SCORE+5);

        when(hackernewsRequester.getItem(anyLong())).thenReturn(Optional.of(hackernewsStory));

        final Story updatedStory = updateStoriesProcessor.process(story);

        assertNotEquals(INITIAL_SCORE, updatedStory.getScore());
        verify(hackernewsRequester).getItem(anyLong());
    }
}
