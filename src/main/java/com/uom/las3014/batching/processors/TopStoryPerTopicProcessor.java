package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.StoriesService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopStoryPerTopicProcessor implements ItemProcessor<Topic, Topic> {
    @Autowired
    private StoriesService storiesService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public Topic process(Topic topic) throws Exception {
        final Story story = storiesService.getTopStoryContainingKeyword(topic.getTopicName()).orElse(null);

        if(story != null) {
            topic.setTopStoryId(story);

            logger.debug(topic.getTopicName() + " has top story id " + story.getStoryId() + " and name " + story.getTitle());
        }

        return topic;
    }
}
