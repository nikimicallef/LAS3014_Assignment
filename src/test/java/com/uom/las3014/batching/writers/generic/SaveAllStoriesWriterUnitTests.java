package com.uom.las3014.batching.writers.generic;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class SaveAllStoriesWriterUnitTests {
    @Mock
    private StoriesService storiesService;
    @InjectMocks
    private SaveAllStoriesWriter saveAllStoriesWriter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        doNothing().when(storiesService).saveAllStories(Matchers.<Iterable<Story>>any());
    }

    @Test
    public void write_emptyList() throws Exception {
        saveAllStoriesWriter.write(new ArrayList<>());

        verify(storiesService).saveAllStories(Matchers.<List<Story>>any());
    }

    @Test
    public void write_listPopulated() throws Exception {
        saveAllStoriesWriter.write(Collections.singletonList(new Story()));

        verify(storiesService).saveAllStories(Matchers.<List<Story>>any());
    }
}
