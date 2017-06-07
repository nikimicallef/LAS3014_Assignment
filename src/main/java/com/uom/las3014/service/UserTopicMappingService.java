package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.UserTopicMappingDaoRepository;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.List;

/**
 * Service for the {@link UserTopicMapping} {@link Entity}.This is the only place which interacts with the {@link UserTopicMappingDaoRepository}
 */
public interface UserTopicMappingService {
    /**
     * @param topic {@link Topic} to retrieved
     * @param interestedToIsAfter {@link UserTopicMapping#getInterestedTo()} is after this timestamp
     * @param interestedFromBefore {@link UserTopicMapping#getInterestedTo()} is before this timestamp
     * @return {@link UserTopicMapping} which satisfy this criteria
     */
    List<UserTopicMapping> findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfterAndInterestedFromBefore(Topic topic,
                                                                                                             Timestamp interestedToIsAfter,
                                                                                                             Timestamp interestedFromBefore);
}
