package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.sql.Date;
import java.util.Set;

/**
 * {@link JpaRepository} for the {@link Digest} {@link Entity}
 */
@Repository
public interface DigestDaoRepository extends JpaRepository<Digest, Long> {
    void deleteDigestByDayOfWeekBefore(final Date timestamp);

    //The below query shows warning HHH90000016 on startup due to the member keyword used. This is a bug and was fixed on
    //a later version of Hibernate https://hibernate.atlassian.net/browse/HHH-11400
    @Query("select d from Digest d where d.dayOfWeek = (select MAX(d.dayOfWeek) from Digest d) and ?1 member d.usersAssignedToDigest")
    Set<Digest> findLatestDigestsForUser(User user);

    //The below query shows warning HHH90000016 on startup due to the member keyword used. This is a bug and was fixed on
    //a later version of Hibernate https://hibernate.atlassian.net/browse/HHH-11400
    @Query("select d from Digest d where d.dayOfWeek >= ?1 and d.dayOfWeek <= ?2 and ?3 member d.usersAssignedToDigest")
    Set<Digest> findGroupOfDigestsBetweenDatesForUser(Date dateFrom, Date dateTo, User user);
}
