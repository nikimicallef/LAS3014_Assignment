package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.springdata.TopicsDaoRepository;

import javax.persistence.Entity;
import java.util.List;

/**
 * Service for the {@link Topic} {@link Entity}.This is the only place which interacts with the {@link TopicsDaoRepository}
 */
public interface TopicService {
    /**
     * Creates a new {@link Topic} or uses the existing one in the database
     * @param topic Topic name
     * @return New or existing {@link Topic}
     */
    Topic createNewTopicIfNotExists(final String topic);

    /**
     * @return All {@link Topic} in database
     */
    List<Topic> getAllTopics();

    /**
     * @param topics All {@link Topic} to be saved
     */
    void saveAllTopics(Iterable<? extends Topic> topics);
}
