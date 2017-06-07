package com.uom.las3014.batching.writers;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.service.DigestsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class WeeklyTopStoriesDigestPerTopicWriterUnitTests {
    @Mock
    private DigestsService digestsService;
    @InjectMocks
    private WeeklyTopStoriesDigestPerTopicWriter weeklyTopStoriesDigestPerTopicWriter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        doNothing().when(digestsService).saveAll(Matchers.<Iterable<Digest>>any());
    }

    @Test
    public void write_outerListEmpty() throws Exception {
        weeklyTopStoriesDigestPerTopicWriter.write(new ArrayList<>());

        verify(digestsService, times(0)).saveAll(Matchers.<List<Digest>>any());
    }

    @Test
    public void write_innerListEmpty() throws Exception {
        List<Digest> innerList = new ArrayList<>();
        List<List<Digest>> outerList = Collections.singletonList(innerList);
        weeklyTopStoriesDigestPerTopicWriter.write(outerList);

        verify(digestsService).saveAll(Matchers.<List<Digest>>any());
    }

    @Test
    public void write_bothListsPopulated() throws Exception {
        List<Digest> innerList = Collections.singletonList(new Digest());
        List<List<Digest>> outerList = Collections.singletonList(innerList);
        weeklyTopStoriesDigestPerTopicWriter.write(outerList);

        verify(digestsService).saveAll(Matchers.<List<Digest>>any());
    }
}
