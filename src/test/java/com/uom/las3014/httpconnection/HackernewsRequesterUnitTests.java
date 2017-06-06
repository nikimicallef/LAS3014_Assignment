package com.uom.las3014.httpconnection;

import com.google.gson.JsonObject;
import com.uom.las3014.httpconnection.HackernewsRequester.ConnectionHandling;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class HackernewsRequesterUnitTests {
    private static final String HACKERNEWS_BASE_URL = "https://localhost/";
    private static final Long ITEM_NUMBER = 123L;
    @Mock
    private HttpURLConnection httpURLConnection;
    @Mock
    private ConnectionHandling connectionHandling;
    @InjectMocks
    private HackernewsRequester hackernewsRequester;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(hackernewsRequester, "baseUrl", HACKERNEWS_BASE_URL);

        when(connectionHandling.createConnection(anyString())).thenReturn(httpURLConnection);
    }

    @Test
    public void getItem_returnesNullText_empty() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream("null".getBytes());

        when(httpURLConnection.getInputStream()).thenReturn(inputStream);

        final Optional<JsonObject> item = hackernewsRequester.getItem(ITEM_NUMBER);

        assertFalse(item.isPresent());
        verify(httpURLConnection).getInputStream();
        verify(connectionHandling).createConnection(anyString());
    }

    @Test
    public void getItem_connectionReturnsIoException_empty() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream("null".getBytes());

        when(httpURLConnection.getInputStream()).thenThrow(new IOException());

        final Optional<JsonObject> item = hackernewsRequester.getItem(ITEM_NUMBER);

        assertFalse(item.isPresent());
        verify(httpURLConnection).getInputStream();
        verify(connectionHandling).createConnection(anyString());
    }

    @Test
    public void getItem_returnesNullText_jsonObject() throws Exception {
        final String jsonBody = "{ \"k1\" : \"v1\" }";

        final InputStream inputStream = new ByteArrayInputStream(jsonBody.getBytes());

        when(httpURLConnection.getInputStream()).thenReturn(inputStream);

        final JsonObject expectedJsonObject = new JsonObject();
        expectedJsonObject.addProperty("k1", "v1");

        final Optional<JsonObject> item = hackernewsRequester.getItem(ITEM_NUMBER);

        assertEquals(expectedJsonObject, item.get());
        verify(httpURLConnection).getInputStream();
        verify(connectionHandling).createConnection(anyString());
    }

    @Test
    public void getNewStories_returnesArrayOfItems_arrayOfItemsResponse() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream("[1, 2]".getBytes());

        when(httpURLConnection.getInputStream()).thenReturn(inputStream);

        final List<String> expectedIds = Arrays.asList("1", "2");

        final Optional<List<String>> itemsReturned = hackernewsRequester.getNewStories();

        assertEquals(expectedIds, itemsReturned.get());
        verify(httpURLConnection).getInputStream();
        verify(connectionHandling).createConnection(anyString());
    }

    @Test
    public void getNewStories_connectionReturnsIoException_empty() throws Exception {
        when(httpURLConnection.getInputStream()).thenThrow(new IOException());

        final Optional<List<String>> itemsReturned = hackernewsRequester.getNewStories();

        assertFalse(itemsReturned.isPresent());
        verify(httpURLConnection).getInputStream();
        verify(connectionHandling).createConnection(anyString());
    }
}
