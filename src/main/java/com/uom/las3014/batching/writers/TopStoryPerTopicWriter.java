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
public class TopStoryPerTopicWriter implements ItemWriter<Topic> {
    @Autowired
    private TopicService topicService;

    @Override
    public void write(List<? extends Topic> list) throws Exception {
        topicService.saveAllTopics(list);
    }
}
