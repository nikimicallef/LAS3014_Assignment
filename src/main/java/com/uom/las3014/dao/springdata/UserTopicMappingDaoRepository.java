package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.UserTopicMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserTopicMappingDaoRepository extends JpaRepository<UserTopicMapping, Long> {
    List<UserTopicMapping> findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(final Topic topic, final Timestamp timestamp);
}
