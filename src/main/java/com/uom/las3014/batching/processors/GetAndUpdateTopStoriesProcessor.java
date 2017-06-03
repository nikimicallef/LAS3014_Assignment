package com.uom.las3014.batching.processors;

import com.google.gson.JsonObject;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class GetAndUpdateTopStoriesProcessor implements ItemProcessor<String, Story> {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private HackernewsRequester hackernewsRequester;

    //TODO: Error handling. What if url or title is too long? What if we get no response from HN etc.
    @Override
    public Story process(String storyId) throws Exception {
        final JsonObject responseJson = hackernewsRequester.getItem(Long.parseLong(storyId)).orElse(null);

        Story story = null;

        if(responseJson != null && !responseJson.has("deleted")){
            story = new Story(responseJson.get("id").getAsLong(),
                                responseJson.get("score").getAsInt(),
                                responseJson.get("title").getAsString(),
                                responseJson.has("url") ? responseJson.get("url").getAsString() : "",
                                new Timestamp(responseJson.get("time").getAsLong() * 1000L));

            logger.debug(story.toString());
        }

        return story;
    }
}
