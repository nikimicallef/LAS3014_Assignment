package com.uom.las3014.batching.processors;

import com.google.gson.JsonObject;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by niki on 17/05/17.
 */
@Component
public class NewStoriesProcessor implements ItemProcessor<String, Story> {

    @Autowired
    private HackernewsRequester hackernewsRequester;

    //TODO: Error handling. What if url or title is too long? What if we get no response from HN etc.
    @Override
    public Story process(String storyId) throws Exception {
        final JsonObject responseJson = hackernewsRequester.getItem(Integer.parseInt(storyId));

        return new Story(responseJson.get("id").getAsLong(),
                        responseJson.get("score").getAsInt(),
                        responseJson.get("title").getAsString(),
                        responseJson.has("url") ? responseJson.get("url").getAsString() : "",
                        new Timestamp(System.currentTimeMillis()));
    }
}
