package com.uom.las3014.batching.readers.generic;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.TopicService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Generic {@link ItemReader} which gets all {@link Topic}. Defined as {@link StepScope} so it is created for each step execution
 */
@Component
@StepScope
public class GetAllTopicsReader implements ItemReader<Topic>{
    private Iterator<Topic> topics;

    @Autowired
    public GetAllTopicsReader(final TopicService topicService) {
        topics = topicService.getAllTopics().iterator();
    }

    @Override
    public Topic read() throws Exception {
        return topics.hasNext() ? topics.next() : null;
    }
}
