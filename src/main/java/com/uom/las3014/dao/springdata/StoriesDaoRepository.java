package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface StoriesDaoRepository extends JpaRepository<Story, Long> {
    Optional<Story> findStoryByStoryId(final Long storyId);

    List<Story> findAllByDateCreatedAfterAndDeletedIs(final Timestamp dateCreatedAfter, final boolean deleted);

//    List<Story> findAllByDateCreatedIsBetweenAndDeletedIs
}
