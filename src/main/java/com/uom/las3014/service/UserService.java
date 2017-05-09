package com.uom.las3014.service;

import com.uom.las3014.api.UserCreateBody;
import com.uom.las3014.api.UserLoginBody;
import com.uom.las3014.dao.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity createNewUser(final UserCreateBody userCreateBody);

    ResponseEntity loginAndGenerateToken(final UserLoginBody userLoginBody);

    ResponseEntity logout(final String sessionToken);

    User getUserFromDbUsingSessionToken(final String sessionToken);

    void invalidateSessionToken(final User user);
}
