package com.uom.las3014.batching.writers;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.TopicService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopStoryPerTopicWriter implements ItemWriter<Pair<Topic, Story>> {
    @Autowired
    private TopicService topicService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void write(List<? extends Pair<Topic, Story>> list) throws Exception {
        list.stream().filter(topicStoryPair -> topicStoryPair.getSecond() != null).forEach(topicStoryPair -> {
                topicStoryPair.getFirst().setTopStoryId(topicStoryPair.getSecond());
                topicService.saveTopic(topicStoryPair.getFirst());
                logger.debug(topicStoryPair.getFirst().getTopicName() + " has top story id " + topicStoryPair.getSecond().getStoryId() + " and name " + topicStoryPair.getSecond().getTitle());
        });
    }
}
