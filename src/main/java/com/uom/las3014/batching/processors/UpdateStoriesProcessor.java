package com.uom.las3014.batching.processors;

import com.google.gson.JsonObject;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateStoriesProcessor implements ItemProcessor<Story, Story>{

    @Autowired
    private HackernewsRequester hackernewsRequester;

    //TODO: Error handling. What if url or title is too long? What if we get no response from HN etc.
    @Override
    public Story process(Story story) throws Exception {
        final JsonObject updatedStory = hackernewsRequester.getItem(story.getStoryId()).orElse(null);

        if(updatedStory!= null && updatedStory.has("deleted")){
            story.setDeleted(true);
        } else if(updatedStory != null && updatedStory.get("score").getAsInt() != story.getScore()){
            story.setScore(updatedStory.get("score").getAsInt());
        }

        return story;
    }
}
