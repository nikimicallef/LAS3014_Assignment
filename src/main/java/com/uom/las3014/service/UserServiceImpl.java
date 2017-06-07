package com.uom.las3014.service;

import com.uom.las3014.api.request.UserCreateRequestBody;
import com.uom.las3014.api.request.UserLoginRequestBody;
import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.api.response.SessionTokenAndMessageResponse;
import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.UsersDaoRepository;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.exceptions.UserAlreadyExistsException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersDaoRepository usersDaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TopicService topicService;

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * {@inheritDoc}
     * @param userCreateRequestBody Credentials of a new user
     * @return Response in model format
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public ResponseEntity<GenericMessageResponse> createNewUser(final UserCreateRequestBody userCreateRequestBody){
        if (userExistsInDbByUsername(userCreateRequestBody.getUsername())) {
            throw new UserAlreadyExistsException("User already exists.");
        } else {
            final Set<Topic> interestedTopics = userCreateRequestBody
                                                    .getInterestedTopics().stream()
                                                    .map(String::toLowerCase)
                                                    .map(topicService::createNewTopicIfNotExists)
                                                    .collect(Collectors.toSet());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createAndSaveNewUser(userCreateRequestBody.getUsername(),
                                               userCreateRequestBody.getPassword(),
                                               interestedTopics));
        }
    }

    /**
     * {@inheritDoc}
     * @param userLoginRequestBody User credentials
     * @return Response in model format
     */
    public ResponseEntity<SessionTokenAndMessageResponse> loginAndGenerateToken(final UserLoginRequestBody userLoginRequestBody){
        final Optional<User> user = getUserFromDb(userLoginRequestBody.getUsername());

        final User retrievedUser = user.orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials."));

        if (!validateUserPassword(userLoginRequestBody.getPassword(), retrievedUser.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials.");
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(generateSessionToken(retrievedUser));
        }
    }

    /**
     * {@inheritDoc}
     * @param user to be logged out
     * @return Response in model format
     */
    public ResponseEntity<GenericMessageResponse> logout(final User user){
        invalidateSessionToken(user);

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new GenericMessageResponse("Logged out successfully."));
    }

    /**
     * {@inheritDoc}
     * @param user to modify
     * @param additions {@link Topic#topicName} tosubscribe to
     * @param removals {@link Topic#topicName} to unsubscribe form
     * @return Response in model format
     */
    @CacheEvict(value = MyCacheManager.TOP_STORY_CACHE, key = "#user")
    public ResponseEntity<GenericMessageResponse> changeInterestedTopics(final User user, final List<String> additions, final List<String> removals){
        if(additions != null){
            additions.stream()
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .map(topicService::createNewTopicIfNotExists)
                    .forEach(topic -> user.getUserTopics()
                            .add(new UserTopicMapping(user, topic, new Timestamp(System.currentTimeMillis()))));
        }

        if(removals != null) {
            final Set<String> topicNamesToRemove = removals.stream()
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .collect(Collectors.toSet());

            user.getUserTopics().stream()
                    .filter(userTopicMapping -> topicNamesToRemove.contains(userTopicMapping.getTopic().getTopicName()))
                    .forEach(item -> {
                        item.setEnabled(false);
                        item.setInterestedTo(new Timestamp(System.currentTimeMillis()));
                    });
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericMessageResponse("Topic changes applied."));
    }

    /**
     * @return All {@link User}
     */
    public List<User> getAllUsers(){
        return usersDaoRepository.findAll();
    }

    /**
     * {@inheritDoc}
     * @param user to be updated
     */
    public void invalidateSessionToken(final User user) {
        user.setSessionToken(null);
        user.setSessionTokenCreated(null);
        user.setSessionTokenLastUsed(null);
    }

    /**
     * Checks whether a user exists with the provided username
     * @param username to find
     * @return true = user exists. false = user does not exist
     */
    private boolean userExistsInDbByUsername(final String username){
        return usersDaoRepository.countUsersByUsername(username) > 0;
    }

    /**
     * @param sessionToken to find
     * @return {@link User} with that session token
     */
    public User getUserFromDbUsingSessionToken(final String sessionToken){
        final Optional<User> userOpt = usersDaoRepository.findUsersBySessionToken(sessionToken);

        return userOpt.orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials."));
    }

    /**
     * Creates and saves new {@link User} with the provided username
     * @param username {@link User#username}
     * @param password {@link User#password}
     * @param interestedTopics {@link UserTopicMapping}
     * @return new {@link User}
     */
    private GenericMessageResponse createAndSaveNewUser(final String username, final String password, final Set<Topic> interestedTopics) {
        final User user = new User(username, passwordEncoder.encode(password));

        interestedTopics.forEach(topic -> {
            final Timestamp interestedFrom = new Timestamp(System.currentTimeMillis());
            user.getUserTopics().add(new UserTopicMapping(user, topic, interestedFrom));
        });

        usersDaoRepository.save(user);

        return new GenericMessageResponse("Created successfully. Login to get your token and use your newly created account.");
    }

    /**
     * @param username {@link User#username} to find
     * @return {@link Optional} with the {@link User}
     */
    private Optional<User> getUserFromDb(final String username){
        return usersDaoRepository.findUsersByUsername(username);
    }

    /**
     * Checks whether the provided and hashed passwords are identical
     * @param rawPassword provided by user
     * @param hashedPassword {@link User#password}
     * @return true = password equal, false = passwords not equal
     */
    private boolean validateUserPassword(final String rawPassword, final String hashedPassword){
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    /**
     * Generates new {@link User#sessionToken} if an invalid one exists or uses the valid one
     * @param user to be modified
     * @return updated {@link User}
     */
    private SessionTokenAndMessageResponse generateSessionToken(final User user) {
        final String sessionTokenResponse;

        if(user.hasActiveSessionToken()){
            user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));

            sessionTokenResponse = user.getSessionToken();
        } else {
            final UUID uuid = UUID.randomUUID();
            final String sessionToken = uuid.toString();

            final Timestamp sessionTokenCreated = new Timestamp(System.currentTimeMillis());

            user.setSessionToken(sessionToken);
            user.setSessionTokenCreated(sessionTokenCreated);

            sessionTokenResponse = sessionToken;
        }

        return new SessionTokenAndMessageResponse("Logged in", sessionTokenResponse);
    }

    /**
     * {@inheritDoc}
     */
    public void invalidateInactiveSessionTokens(){
        usersDaoRepository.streamUsersBySessionTokenNotNull()
                .filter(user -> !user.hasActiveSessionToken())
                .peek(user -> logger.debug("Invalidating session token " + user.getSessionToken() + " for user ID "+ user.getUserId()))
                .forEach(this::invalidateSessionToken);
    }

    /**
     * {@inheritDoc}
     * @param user {@link User} to be updated
     */
    public void updateSessionTokenLastUsed(final User user){
        user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));
    }
}
