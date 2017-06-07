package com.uom.las3014.service;

import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.springdata.TopicsDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TopicServiceImpl implements TopicService{
    @Autowired
    private TopicsDaoRepository topicsDaoRepository;

    @Autowired
    private StoriesService storiesService;

    /**
     * {@inheritDoc}
     * @param topicName Topic name
     * @return New or existing {@link Topic}
     */
    @Cacheable(MyCacheManager.TOPIC_CACHE)
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Topic createNewTopicIfNotExists(final String topicName){
        final Optional<Topic> existingTopic = topicsDaoRepository.findTopicsByTopicName(topicName);

        if(existingTopic.isPresent()){
            return existingTopic.get();
        } else {
            final Topic newTopic = topicsDaoRepository.save(new Topic(topicName));

            setTopStoryForTopic(newTopic);

            return newTopic;
        }
    }

    /**
     * @return All {@link Topic} in database
     */
    @Override
    public List<Topic> getAllTopics() {
        return topicsDaoRepository.findAll();
    }

    /**
     * @param topics All {@link Topic} to be saved
     */
    @Override
    public void saveAllTopics(Iterable<? extends Topic> topics){
        topicsDaoRepository.save(topics);
    }

    /**
     * Sets the top {@link Story} for a {@link Topic}
     * @param topic To be updated
     */
    @Async
    private void setTopStoryForTopic(final Topic topic){
        final List<Story> topStoryContainingKeyword = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(topic.getTopicName(), new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24)));

        final Optional<Story> topStoryOpt = topStoryContainingKeyword.stream().max(Comparator.comparing(Story::getScore));

        topStoryOpt.ifPresent(topic::setTopStoryId);
    }
}
