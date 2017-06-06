package com.uom.las3014.httpconnection;

import com.uom.las3014.httpconnection.HackernewsRequester.ConnectionHandling;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ConnectionHandlingUnitTests {
    private static final String HTTP_ADDRESS = "http://www.um.edu.mt";
    private HackernewsRequester hackernewsRequester = new HackernewsRequester();
    private ConnectionHandling connectionHandling = hackernewsRequester.new ConnectionHandling();

    @Test
    public void createConnection_validUrl_connectionCreated() throws IOException {
        final URL expectedUrl = new URL(HTTP_ADDRESS);
        final HttpURLConnection expectedHttpUrlConnection = (HttpURLConnection) expectedUrl.openConnection();
        expectedHttpUrlConnection.setRequestMethod("GET");

        final HttpURLConnection actualConnection = connectionHandling.createConnection(HTTP_ADDRESS);

        assertEquals(expectedHttpUrlConnection.getRequestMethod(), actualConnection.getRequestMethod());
    }

    @Test(expected = IOException.class)
    public void createConnection_invalidUrl_ioException() throws IOException {
        final HttpURLConnection actualConnection = connectionHandling.createConnection("testAdress");
    }
}
