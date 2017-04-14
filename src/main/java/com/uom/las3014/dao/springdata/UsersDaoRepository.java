package com.uom.las3014.dao.springdata;

import com.uom.las3014.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDaoRepository extends JpaRepository<User, String>{
    User findUsersByUsername(final String username);

    User findUsersBySession_token(final String sessionToken);
}
