package com.uom.las3014.batching.writers;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.TopicService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Specific {@link ItemWriter} which saves all {@link Topic}
 */
@Component
public class TopStoryPerTopicWriter implements ItemWriter<Topic> {
    @Autowired
    private TopicService topicService;

    @Override
    public void write(final List<? extends Topic> list) throws Exception {
        topicService.saveAllTopics(list);
    }
}
