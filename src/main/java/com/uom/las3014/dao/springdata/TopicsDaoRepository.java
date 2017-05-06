package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicsDaoRepository extends JpaRepository<Topic, Integer> {
}
