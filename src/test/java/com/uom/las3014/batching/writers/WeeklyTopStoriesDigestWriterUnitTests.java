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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class WeeklyTopStoriesDigestWriterUnitTests {
    @Mock
    private DigestsService digestsService;
    @InjectMocks
    private WeeklyTopStoriesDigestWriter weeklyTopStoriesDigestWriter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        doNothing().when(digestsService).saveAll(Matchers.<Iterable<Digest>>any());
    }

    @Test
    public void write_outerListEmpty() throws Exception {
        weeklyTopStoriesDigestWriter.write(new ArrayList<>());

        verify(digestsService).saveAll(Matchers.<List<Digest>>any());
    }

    @Test
    public void write_innerListEmpty() throws Exception {
        weeklyTopStoriesDigestWriter.write(Collections.singletonList(new Digest()));

        verify(digestsService).saveAll(Matchers.<List<Digest>>any());
    }
}
