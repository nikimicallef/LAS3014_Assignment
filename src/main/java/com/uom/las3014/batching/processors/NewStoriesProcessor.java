package com.uom.las3014.batching.processors;

import com.google.gson.JsonObject;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by niki on 17/05/17.
 */
@Component
public class NewStoriesProcessor implements ItemProcessor<String, Story> {

    @Autowired
    private HackernewsRequester hackernewsRequester;

    @Override
    public Story process(String storyId) throws Exception {
        final JsonObject responseJson = hackernewsRequester.getItem(Integer.parseInt(storyId));

        return new Story(responseJson.get("id").getAsInt(), responseJson.get("score").getAsInt(), responseJson.get("title").getAsString());
    }
}
