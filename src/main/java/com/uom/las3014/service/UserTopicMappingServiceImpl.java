package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.UserTopicMappingDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;


@Service
@Transactional
public class UserTopicMappingServiceImpl implements UserTopicMappingService {
    @Autowired
    private UserTopicMappingDaoRepository userTopicMappingDaoRepository;

    /**
     * @param topic {@link Topic} to retrieved
     * @param interestedToIsAfter {@link UserTopicMapping#getInterestedTo()} is after this timestamp
     * @param interestedFromBefore {@link UserTopicMapping#getInterestedTo()} is before this timestamp
     * @return {@link UserTopicMapping} which satisfy this criteria
     */
    @Override
    public List<UserTopicMapping> findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfterAndInterestedFromBefore(final Topic topic,
                                                                                                                    final Timestamp interestedToIsAfter,
                                                                                                                    final Timestamp interestedFromBefore) {
        return userTopicMappingDaoRepository.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfterAndInterestedFromBefore(topic,
                                                                                                                               interestedToIsAfter,
                                                                                                                               interestedFromBefore);
    }
}
