package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Digest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface DigestDaoRepository extends JpaRepository<Digest, Long> {
    void deleteDigestByDayOfWeekBefore(final Timestamp timestamp);
}
