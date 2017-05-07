package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;

public interface TopicService {
    Topic createNewTopicIfNotExists(final String topic);
}
