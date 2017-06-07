package com.uom.las3014.httpconnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles connection to the Hacker News API
 */
@Service
public class HackernewsRequester {
    @Value("${com.uom.las3014.hackernews.base.url}")
    private String baseUrl;
    @Autowired
    private ConnectionHandling connectionHandling;

    /**
     * Gets the information for a specified item for a provided item id
     * @param itemNo Id of the item to be retrieved
     * @return Response retrieved as a {@link JsonObject} wrapped in an {@link Optional}. If the API returns nothing then null is returned
     * @throws IOException Error when connectiong to the Hacker News API
     */
    public Optional<JsonObject> getItem(final Long itemNo) throws IOException {
        final HttpURLConnection httpUrlConnection = connectionHandling.createConnection(baseUrl + "item/" + itemNo + ".json");

        final String responseBody = getResponseFromHackernews(httpUrlConnection);

        //Sometimes HN API returns null for items which (do not) exist so the below code caters for this anomaly
        if (responseBody == null || responseBody.equals("null")) {
            return Optional.empty();
        } else {
            return Optional.of(new JsonParser().parse(responseBody).getAsJsonObject());
        }
    }

    /**
     * Gets the newest 500 story ids from the Hacker News API
     * @return {@link List} of {@link String} with the 500 newest story ids
     * @throws IOException Error connecting to Hacker News API
     */
    public Optional<List<String>> getNewStories() throws IOException {
        final HttpURLConnection httpUrlConnection = connectionHandling.createConnection(baseUrl + "newstories.json");

        final String responseBody = getResponseFromHackernews(httpUrlConnection);

        if (responseBody == null) {
            return Optional.empty();
        } else {
            return Optional.of(Arrays.asList(responseBody.substring(1, responseBody.length() - 1).split(",")));
        }
    }

    private String getResponseFromHackernews(final HttpURLConnection httpUrlConnection) {
        //Required since sometimes the GET story for a specific valid ID will still get a connect exception.
        //In that case just return null
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            return br.lines().collect(Collectors.joining());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Handles theconnection with an external system via HTTP
     */
    @Component
    public class ConnectionHandling{
        public HttpURLConnection createConnection(final String connectionUrl) throws IOException {
            final HttpURLConnection httpUrlConnection;

            final URL url = new URL(connectionUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            return httpUrlConnection;
        }
    }
}
