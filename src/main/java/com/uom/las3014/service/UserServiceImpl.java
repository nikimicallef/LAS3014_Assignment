package com.uom.las3014.service;

import com.uom.las3014.api.UserCreateBody;
import com.uom.las3014.api.UserLoginBody;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.UsersDaoRepository;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.exceptions.UserAlreadyExistsException;
import com.uom.las3014.resources.Resources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersDaoRepository usersDaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TopicServiceImpl topicService;

    private Map<String, String> jsonBodyKeyValuePair;
    private final Log logger = LogFactory.getLog(this.getClass());

    public ResponseEntity createNewUser(final UserCreateBody userCreateBody){
        //TODO: Convert this to AOP. But we need this once so is AOP useful here??
        if (userExistsInDbByUsername(userCreateBody.getUsername())) {
            throw new UserAlreadyExistsException();
        } else {
            final Set<Topic> interestedTopics = userCreateBody.getInterestedTopics().stream().map(String::toLowerCase).map(topicService::createNewTopicIfNotExists).collect(Collectors.toSet());

            jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.putAll(createAndSaveNewUser(userCreateBody.getUsername(), userCreateBody.getPassword(), interestedTopics));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }

    public ResponseEntity loginAndGenerateToken(final UserLoginBody userLoginBody){
        //TODO: Convert this to AOP. But we need this once so is AOP useful here??
        final Optional<User> user = getUserFromDb(userLoginBody.getUsername());

        final User retrievedUser = user.orElseThrow(InvalidCredentialsException::new);

        if (!validateUserPassword(userLoginBody.getPassword(), retrievedUser.getPassword())) {
            throw new InvalidCredentialsException();
        } else {
            jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.putAll(generateSessionToken(retrievedUser));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }

    public ResponseEntity logout(final String sessionToken){
        //TODO: Get user from pointcut
        final Optional<User> user = getUserFromDbUsingSessionToken(sessionToken);

        final User retrievedUser = user.orElseThrow(InvalidCredentialsException::new);

        invalidateSessionToken(retrievedUser);

        jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("message", "Logged out successfully.");

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
    }

    public ResponseEntity changeInterestedTopics(final String sessionToken, final List<String> additions, final List<String> removals){
        //TODO: Get user from pointcut
        final Optional<User> user = getUserFromDbUsingSessionToken(sessionToken);

        final User retrievedUser = user.orElseThrow(InvalidCredentialsException::new);

        if(additions != null){
            additions.stream()
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .map(topicService::createNewTopicIfNotExists)
                    .forEach(topic -> retrievedUser.getUserTopics()
                            .add(new UserTopicMapping(retrievedUser, topic, new Timestamp(System.currentTimeMillis()))));
        }

        if(removals != null) {
            final Set<String> topicNamesToRemove = removals.stream()
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .collect(Collectors.toSet());

            retrievedUser.getUserTopics().stream()
                    .filter(userTopicMapping -> topicNamesToRemove.contains(userTopicMapping.getTopic().getTopicName()))
                    .forEach(item -> {
                        item.setEnabled(false);
                        item.setInterestedTo(new Timestamp(System.currentTimeMillis()));
                    });
        }

        //TODO: Convert to AOP around
        retrievedUser.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));

        jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("message", "Topic changes applied.");

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
    }

    public void invalidateSessionToken(final User user) {
        user.setSessionToken(null);
        user.setSessionTokenCreated(null);
        user.setSessionTokenLastUsed(null);
    }

    private boolean userExistsInDbByUsername(final String username){
        return usersDaoRepository.countUsersByUsername(username) > 0;
    }

    public Optional<User> getUserFromDbUsingSessionToken(final String sessionToken){
        return usersDaoRepository.findUsersBySessionToken(sessionToken);
    }

    private Map<String, String> createAndSaveNewUser(final String username, final String password, final Set<Topic> interestedTopics) {
        final User user = new User(username, passwordEncoder.encode(password));

        interestedTopics.forEach(topic -> user.getUserTopics().add(new UserTopicMapping(user, topic, new Timestamp(System.currentTimeMillis()))));

        usersDaoRepository.save(user);

        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("message", "Created successfully. Login to get your token and use your newly created account.");

        return jsonBodyKeyValuePair;
    }

    private Optional<User> getUserFromDb(final String username){
        return usersDaoRepository.findUsersByUsername(username);
    }

    private boolean validateUserPassword(final String rawPassword, final String hashedPassword){
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    private Map<String, String> generateSessionToken(final User user) {
        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();

        if(user.hasActiveSessionToken()){
            user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));

            jsonBodyKeyValuePair.put("sessionToken", user.getSessionToken());
        } else {
            final UUID uuid = UUID.randomUUID();
            final String sessionToken = uuid.toString();

            final Timestamp sessionTokenCreated = new Timestamp(System.currentTimeMillis());

            user.setSessionToken(sessionToken);
            user.setSessionTokenCreated(sessionTokenCreated);
            user.setSessionTokenLastUsed(null);

            jsonBodyKeyValuePair.put("sessionToken", sessionToken);
        }

        jsonBodyKeyValuePair.put("message", "Logged in.");

        return jsonBodyKeyValuePair;
    }

    public void invalidateInactiveSessionTokens(){
        usersDaoRepository.streamUsersBySessionTokenNotNull()
                .filter(user -> !user.hasActiveSessionToken())
                .peek(user -> logger.debug("Invalidating session token " + user.getSessionToken() + " for user ID "+ user.getUserId()))
                .forEach(this::invalidateSessionToken);
    }
}
