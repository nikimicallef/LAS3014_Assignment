package com.uom.las3014.service;

import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.dao.Topic;
import org.springframework.http.ResponseEntity;

public interface TopicService {
    Topic createNewTopicIfNotExists(final String topic);
}
