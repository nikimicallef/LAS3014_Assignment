package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.UserTopicMapping;

import java.sql.Timestamp;
import java.util.List;

public interface UserTopicMappingService {
    List<UserTopicMapping> findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(final Topic topic, final Timestamp timestamp);
}
