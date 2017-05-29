package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.Digest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigestsDaoRepository extends JpaRepository<Digest, Long> {
}
