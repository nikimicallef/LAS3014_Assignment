package com.uom.las3014.httpconnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class HackernewsRequester {
    @Value("${com.uom.las3014.hackernews.base.url}")
    private String baseUrl;

    public Optional<JsonObject> getItem(final Long itemNo){
        final HttpURLConnection httpUrlConnection;

        try {
            final URL url = new URL(baseUrl + "item/" + itemNo + ".json");
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
        } catch (IOException e){
            throw new IllegalArgumentException();
        }

        final String responseBody = getResponseFromHackernews(httpUrlConnection);

        //Sometimes HN API returns null for items which do not exist.
        if(responseBody == null || responseBody.equals("null")){
            return Optional.empty();
        } else {
            return Optional.of(new JsonParser().parse(responseBody).getAsJsonObject());
        }
    }

    public Optional<List<String>> getNewStories() {
        final HttpURLConnection httpUrlConnection;

        try {
            final URL url = new URL(baseUrl + "newstories.json");
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        final String responseBody = getResponseFromHackernews(httpUrlConnection);

        if(responseBody == null){
            return Optional.empty();
        } else {
            return Optional.of(Arrays.asList(responseBody.substring(1, responseBody.length()-1).split(",")));
        }
    }

    private String getResponseFromHackernews(HttpURLConnection httpUrlConnection) {
        int counter = 1;
        String responseBody = null;
        do{
            try {
                final BufferedReader br = new BufferedReader(new InputStreamReader((httpUrlConnection.getInputStream())));
                responseBody = br.lines().collect(Collectors.joining());
            } catch (IOException e){
                counter++;
            }
        }while(responseBody == null && counter < 5);
        return responseBody;
    }
}
