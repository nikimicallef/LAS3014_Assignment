package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@link JpaRepository} for the {@link User} {@link Entity}
 */
@Repository
public interface UsersDaoRepository extends JpaRepository<User, Long>{
    Optional<User> findUsersByUsername(final String username);

    Optional<User> findUsersBySessionToken(final String sessionToken);

    Integer countUsersByUsername(final String username);

    Stream<User> streamUsersBySessionTokenNotNull();
}
