package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;

import java.util.List;

public interface TopicService {
    Topic createNewTopicIfNotExists(final String topic);

    List<Topic> getAllTopics();

    void saveTopic(Topic topic);

//    void saveTopics(List<Topic> topics);
}
