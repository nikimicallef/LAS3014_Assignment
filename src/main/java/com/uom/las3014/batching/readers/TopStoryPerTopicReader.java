package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.TopicService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@StepScope
public class TopStoryPerTopicReader implements ItemReader<Topic>{
    private Iterator<Topic> topics;

    @Autowired
    public TopStoryPerTopicReader(final TopicService topicService) {
        topics = topicService.getAllTopics().iterator();
    }

    @Override
    public Topic read() throws Exception {
        return topics.hasNext() ? topics.next() : null;
    }
}
