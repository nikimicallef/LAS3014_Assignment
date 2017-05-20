package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoriesDaoRepository extends JpaRepository<Story, Long> {
    Optional<Story> findStoryByStoryId(final Long storyId);
}
