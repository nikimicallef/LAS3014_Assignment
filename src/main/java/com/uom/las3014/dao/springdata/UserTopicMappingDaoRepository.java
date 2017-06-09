package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.UserTopicMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.List;

/**
 * {@link JpaRepository} for the {@link UserTopicMapping} {@link Entity}
 */
@Repository
public interface UserTopicMappingDaoRepository extends JpaRepository<UserTopicMapping, Long> {
    List<UserTopicMapping> findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfterAndInterestedFromBefore(Topic topic,
                                                                                                             Timestamp interestedToIsAfter,
                                                                                                             Timestamp interestedFromBefore);
}
