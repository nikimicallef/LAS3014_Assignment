package com.uom.las3014.service;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.springdata.TopicsDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TopicServiceImpl implements TopicService{
    @Autowired
    private TopicsDaoRepository topicsDaoRepository;

    @Autowired
    private StoriesService storiesService;

    @Cacheable("topicPojo")
    public Topic createNewTopicIfNotExists(final String topicName){
        //TODO: Change with CREATE-IF-NOT-EXISTS instead of 2 queries.
        final Optional<Topic> existingTopic = topicsDaoRepository.findTopicsByTopicName(topicName);

        if(existingTopic.isPresent()){
            return existingTopic.get();
        } else {
            final Topic newTopic = topicsDaoRepository.save(new Topic(topicName));

            setTopStoryForTopic(newTopic);

            return newTopic;
        }
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicsDaoRepository.findAll();
    }

    @Override
    public void saveTopic(Topic topic) {
        topicsDaoRepository.save(topic);
    }

//    @Override
//    public void saveTopics(List<Topic> topics) {
//        topicsDaoRepository.save(topics);
//    }

    @Async
    private void setTopStoryForTopic(final Topic topic){
        final Optional<Story> topStoryContainingKeyword = storiesService.getTopStoryContainingKeywordAndCreatedInLastWeek(topic.getTopicName());

        topStoryContainingKeyword.ifPresent(topic::setTopStoryId);
    }
}
