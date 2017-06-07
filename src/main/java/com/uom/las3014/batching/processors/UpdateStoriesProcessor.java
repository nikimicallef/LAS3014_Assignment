package com.uom.las3014.batching.processors;

import com.google.gson.JsonObject;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Specific {@link ItemProcessor} which queries the {@link HackernewsRequester} for a specific {@link Story} and the
 *     {@link Story#deleted} and/or {@link Story#score} is updated
 */
@Component
public class UpdateStoriesProcessor implements ItemProcessor<Story, Story>{
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private HackernewsRequester hackernewsRequester;

    @Override
    public Story process(final Story story) throws Exception {
        final JsonObject updatedStory = hackernewsRequester.getItem(story.getStoryId()).orElse(null);

        logger.debug(story.getStoryId() + " <-- ID of story attemping to be updated");


        if(updatedStory!= null && updatedStory.has("deleted")){
            story.setDeleted(true);
        } else if(updatedStory != null && updatedStory.get("score").getAsInt() != story.getScore()){
            logger.debug(story.getStoryId() + " story's score has been updated from " + story.getScore() + " to " + updatedStory.get("score").getAsInt());

            story.setScore(updatedStory.get("score").getAsInt());
        }

        return story;
    }
}
