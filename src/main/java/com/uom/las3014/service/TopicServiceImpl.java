package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.springdata.TopicsDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TopicServiceImpl implements TopicService{
    @Autowired
    private TopicsDaoRepository topicsDaoRepository;

    public Topic createNewTopicIfNotExists(final String topicName){
        //TODO: Change with CREATE-IF-NOT-EXISTS instead of 2 queries.
        final Topic existingTopic = topicsDaoRepository.findTopicsByTopicName(topicName);
        if(existingTopic == null) {
            final Topic newTopic = new Topic(topicName);
            //TODO: Investigate if we can replace this with sets
            return topicsDaoRepository.save(newTopic);
        } else {
            return existingTopic;
        }
    }
}
