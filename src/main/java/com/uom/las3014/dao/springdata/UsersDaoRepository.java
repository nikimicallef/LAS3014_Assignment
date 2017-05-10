package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UsersDaoRepository extends JpaRepository<User, Integer>{
    Optional<User> findUsersByUsername(final String username);

    Optional<User> findUsersBySessionToken(final String sessionToken);

    Integer countUsersByUsername(final String username);

    Stream<User> streamUsersBySessionTokenNotNull();
}
