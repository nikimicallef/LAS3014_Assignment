package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.List;

/**
 * {@link JpaRepository} for the {@link Story} {@link Entity}
 */
@Repository
public interface StoriesDaoRepository extends JpaRepository<Story, Long> {
    List<Story> findAllByDateCreatedIsAfterAndDeletedIsFalse(final Timestamp createdAfter);

    List<Story> findAllByTitleContainingAndDateCreatedIsAfterAndDeletedIsFalse(final String keyword,
                                                                               final Timestamp createdAfter);

    List<Story> findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(final Timestamp createdAfter,
                                                                                final Integer scoreGreaterThan);

    @Modifying
    @Query("delete from Story s where s.dateCreated < ?1 and s.digests IS EMPTY")
    void deleteByDateCreatedBeforeAndDigestsEmpty(final Timestamp timestamp);
}