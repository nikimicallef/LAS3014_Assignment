package com.uom.las3014.service;

import com.uom.las3014.api.request.UserCreateRequestBody;
import com.uom.las3014.api.request.UserLoginRequestBody;
import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.api.response.SessionTokenAndMessageResponse;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.UsersDaoRepository;
import org.springframework.http.ResponseEntity;

import javax.persistence.Entity;
import java.util.List;

/**
 * Service for the {@link User} {@link Entity}.This is the only place which interacts with the {@link UsersDaoRepository}
 */
public interface UserService {
    /**
     * Creates a new user for the provided credentials
     * @param userCreateRequestBody Credentials of a new user
     * @return Response in model format
     */
    ResponseEntity<GenericMessageResponse> createNewUser(UserCreateRequestBody userCreateRequestBody);

    /**
     * Logs in the user by providing a {@link User#sessionToken}
     * @param userLoginRequestBody User credentials
     * @return Response in model format
     */
    ResponseEntity<SessionTokenAndMessageResponse> loginAndGenerateToken(UserLoginRequestBody userLoginRequestBody);

    /**
     * Logouts {@link User} by invalidating the {@link User#sessionToken}
     * @param user to be logged out
     * @return Response in model format
     */
    ResponseEntity<GenericMessageResponse> logout(User user);

    /**
     * Subscribed or unsubscribes a {@link User} from the provided {@link Topic#topicName}
     * @param user to modify
     * @param additions {@link Topic#topicName} tosubscribe to
     * @param removals {@link Topic#topicName} to unsubscribe form
     * @return Response in model format
     */
    ResponseEntity<GenericMessageResponse> changeInterestedTopics(User user, List<String> additions, List<String> removals);

    /**
     * @param sessionToken to find
     * @return {@link User} with that session token
     */
    User getUserFromDbUsingSessionToken(String sessionToken);

    /**
     * @return All {@link User}
     */
    List<User> getAllUsers();

    /**
     * Sets {@link User#sessionToken}, {@link User#sessionTokenCreated} and {@link User#sessionTokenLastUsed} to null
     * @param user to be updated
     */
    void invalidateSessionToken(User user);

    /**
     * Finds all {@link User#sessionToken} which are invalid and they are invalidated
     */
    void invalidateInactiveSessionTokens();

    /**
     * Sets the {@link User#sessionTokenLastUsed} to current time
     * @param user {@link User} to be updated
     */
    void updateSessionTokenLastUsed(User user);
}
