package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.Optional;

/**
 * {@link JpaRepository} for the {@link Topic} {@link Entity}
 */
@Repository
public interface TopicsDaoRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findTopicsByTopicName(final String topicName);
}
