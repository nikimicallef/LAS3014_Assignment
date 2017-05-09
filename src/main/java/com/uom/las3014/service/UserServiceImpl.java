package com.uom.las3014.service;

import com.uom.las3014.api.UserCreateBody;
import com.uom.las3014.api.UserLoginBody;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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
        final User user = getUserFromDb(userLoginBody.getUsername());

        if (user != null && !validateUserPassword(userLoginBody.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        } else {
            jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.putAll(generateSessionToken(user));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }

    public ResponseEntity logout(final String sessionToken){
        //TODO: Get user from pointcut
        final User user = getUserFromDbUsingSessionToken(sessionToken);

        invalidateSessionToken(user);

        jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("message", "Logged out successfully.");

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
    }

    public ResponseEntity changeInterestedTopics(final String sessionToken, final List<String> additions, final List<String> removals){
        //TODO: Get user from pointcut
        final User user = getUserFromDbUsingSessionToken(sessionToken);

        if(additions != null){
            additions.stream().map(String::toLowerCase).map(topicService::createNewTopicIfNotExists).forEach(topic -> user.getTopics().add(topic));
        }

        if(removals != null) {
            removals.stream().map(String::toLowerCase).map(topicService::createNewTopicIfNotExists).forEach(topic -> user.getTopics().remove(topic));
        }

        //TODO: Convert to AOP around
        user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));

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

    public User getUserFromDbUsingSessionToken(final String sessionToken){
        return usersDaoRepository.findUsersBySessionToken(sessionToken);
    }

    private Map<String, String> createAndSaveNewUser(final String username, final String password, final Set<Topic> interestedTopics) {
        usersDaoRepository.save(new User(username, passwordEncoder.encode(password), interestedTopics));

        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("message", "Created successfully. Login to get your token and use your newly created account.");

        return jsonBodyKeyValuePair;
    }

    private User getUserFromDb(final String username){
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

    //TODO: Set to run every 10 minutes
    //TODO: Move to scheduling module
    @Scheduled(fixedDelay = 600000)
    //@Scheduled(fixedDelay = 5000)
    public void invalidateInactiveSessionTokensScheduledTask(){
        logger.debug("Running scheduled task which invalidates inactive session tokens.");
        usersDaoRepository.streamUsersBySessionTokenNotNull().filter(user -> !user.hasActiveSessionToken()).forEach(this::invalidateSessionToken);
    }
}
