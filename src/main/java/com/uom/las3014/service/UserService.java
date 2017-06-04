package com.uom.las3014.service;

import com.uom.las3014.api.request.UserCreateRequestBody;
import com.uom.las3014.api.request.UserLoginRequestBody;
import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.api.response.SessionTokenAndMessageResponse;
import com.uom.las3014.dao.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<GenericMessageResponse> createNewUser(final UserCreateRequestBody userCreateRequestBody);

    ResponseEntity<SessionTokenAndMessageResponse> loginAndGenerateToken(final UserLoginRequestBody userLoginRequestBody);

    ResponseEntity<GenericMessageResponse> logout(final User user);

    ResponseEntity<GenericMessageResponse> changeInterestedTopics(final User user, final List<String> additions, final List<String> removals);

    User getUserFromDbUsingSessionToken(final String sessionToken);

    List<User> getAllUsers();

    void invalidateSessionToken(final User user);

    void invalidateInactiveSessionTokens();

    void updateSessionTokenLastUsed(final User user);
}
