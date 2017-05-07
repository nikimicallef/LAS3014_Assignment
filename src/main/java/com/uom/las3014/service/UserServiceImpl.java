package com.uom.las3014.service;

import com.uom.las3014.api.UserCreateBody;
import com.uom.las3014.api.UserLoginBody;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.UsersDaoRepository;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.exceptions.UserAlreadyExistsException;
import com.uom.las3014.resources.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
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
    private TopicServiceImpl topicService;

    private final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();

    public ResponseEntity createNewUser(final UserCreateBody userCreateBody){
        if (userExistsInDbByUsername(userCreateBody.getUsername())) {
            throw new UserAlreadyExistsException();
        } else {
            final Set<Topic> interestedTopics = userCreateBody.getInterestedTopics().stream().map(topicService::createNewTopicIfNotExists).collect(Collectors.toSet());

            jsonBodyKeyValuePair.putAll(createAndSaveNewUser(userCreateBody.getUsername(), userCreateBody.getPassword(), interestedTopics));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }

    public ResponseEntity loginAndGenerateToken(final UserLoginBody userLoginBody){
        final User user = getUserFromDb(userLoginBody.getUsername());

        if (user != null && !validateUserPassword(userLoginBody.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        } else {
            jsonBodyKeyValuePair.putAll(generateSessionToken(user));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }

    public ResponseEntity logout(final String sessionToken){
        final User user = getUserFromDbUsingSessionToken(sessionToken);

        if (user != null) {
            user.setSessionToken(null);
            user.setSessionTokenCreated(null);
            user.setSessionTokenLastUsed(null);

            usersDaoRepository.save(user);

            jsonBodyKeyValuePair.put("message", "Logged out successfully.");

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        } else {
            throw new InvalidCredentialsException();
        }
    }

    private boolean userExistsInDbByUsername(final String username){
        return usersDaoRepository.countUsersByUsername(username) > 0;
    }

    private User getUserFromDbUsingSessionToken(final String sessionToken){
        return usersDaoRepository.findUsersBySessionToken(sessionToken);
    }

    private Map<String, String> createAndSaveNewUser(final String username, final String password, final Set<Topic> interestedTopics) {
        final User newUser = new User(username, passwordEncoder.encode(password), interestedTopics);

        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();

        try {
            usersDaoRepository.save(newUser);
        } catch (Exception e) {
            jsonBodyKeyValuePair.put("error", "User can't be created at the moment.");

            return jsonBodyKeyValuePair;
        }

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
            final Timestamp sessionTokenLastUsed = new Timestamp(System.currentTimeMillis());

            user.setSessionTokenLastUsed(sessionTokenLastUsed);

            try {
                usersDaoRepository.save(user);

                jsonBodyKeyValuePair.put("message", "Logged in.");
                jsonBodyKeyValuePair.put("sessionToken", user.getSessionToken());
            } catch (Exception e){
                jsonBodyKeyValuePair.put("error", "User can't be logged in at the moment.");
            }
        } else {
            final UUID uuid = UUID.randomUUID();
            final String sessionToken = uuid.toString();

            final Timestamp sessionTokenCreated = new Timestamp(System.currentTimeMillis());

            user.setSessionToken(sessionToken);
            user.setSessionTokenCreated(sessionTokenCreated);
            user.setSessionTokenLastUsed(null);

            try {
                usersDaoRepository.save(user);

                jsonBodyKeyValuePair.put("message", "Logged in.");
                jsonBodyKeyValuePair.put("sessionToken", sessionToken);
            } catch (Exception e){
                jsonBodyKeyValuePair.put("error", "User can't be logged in at the moment.");
            }
        }

        return jsonBodyKeyValuePair;
    }

}
