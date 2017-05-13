package com.uom.las3014.service;

import com.uom.las3014.api.request.UserCreateRequestBody;
import com.uom.las3014.api.request.UserLoginRequestBody;
import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.api.response.SessionTokenAndMessageResponse;
import com.uom.las3014.dao.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseEntity<GenericMessageResponse> createNewUser(final UserCreateRequestBody userCreateRequestBody);

    ResponseEntity<SessionTokenAndMessageResponse> loginAndGenerateToken(final UserLoginRequestBody userLoginRequestBody);

    ResponseEntity<GenericMessageResponse> logout(final String sessionToken);

    ResponseEntity<GenericMessageResponse> changeInterestedTopics(final String sessionToken, final List<String> additions, final List<String> removals);

    Optional<User> getUserFromDbUsingSessionToken(final String sessionToken);

    void invalidateSessionToken(final User user);

    void invalidateInactiveSessionTokens();
}
