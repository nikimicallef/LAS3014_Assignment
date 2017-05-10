package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicsDaoRepository extends JpaRepository<Topic, Integer> {
    Optional<Topic> findTopicsByTopicName(final String topicName);
}
