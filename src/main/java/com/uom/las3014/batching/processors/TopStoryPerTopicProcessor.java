package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.StoriesService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class TopStoryPerTopicProcessor implements ItemProcessor<Topic, Pair<Topic, Story>> {
    @Autowired
    private StoriesService storiesService;

    @Override
    public Pair<Topic, Story> process(Topic topic) throws Exception {
        final Story story = storiesService.getTopStoryContainingKeyword(topic.getTopicName()).orElse(null);

        return Pair.of(topic, story);
    }
}
