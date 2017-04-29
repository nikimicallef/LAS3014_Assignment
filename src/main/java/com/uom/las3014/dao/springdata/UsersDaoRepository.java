package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDaoRepository extends JpaRepository<User, String>{
    User findUsersByUsername(final String username);

    User findUsersBySessionToken(final String sessionToken);

    Integer countUsersByUsername(final String username);
}
