package com.uom.las3014.batching.processors;

import com.google.gson.JsonObject;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateStoriesProcessor implements ItemProcessor<Story, Story>{
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private HackernewsRequester hackernewsRequester;

    //TODO: Error handling. What if url or title is too long? What if we get no response from HN etc.
    @Override
    public Story process(Story story) throws Exception {
        final JsonObject updatedStory = hackernewsRequester.getItem(story.getStoryId()).orElse(null);

        if(updatedStory!= null && updatedStory.has("deleted")){
            story.setDeleted(true);
        } else if(updatedStory != null && updatedStory.get("score").getAsInt() != story.getScore()){
            logger.debug(story.getStoryId() + " story's score has been updated from " + story.getScore() + " to " + updatedStory.get("score").getAsInt());

            story.setScore(updatedStory.get("score").getAsInt());
        }

        return story;
    }
}
