package com.uom.las3014.service;

import com.uom.las3014.api.request.UserCreateRequestBody;
import com.uom.las3014.api.request.UserLoginRequestBody;
import com.uom.las3014.api.request.UserTopicsRequestBody;
import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.api.response.SessionTokenAndMessageResponse;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.UsersDaoRepository;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.exceptions.UserAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class UserServiceImplUnitTests {
    private static final String USERNAME = "TestUser";
    private static final String PASSWORD = "TestPassword";
    private static final String TOPIC1_NAME = "TestTopic1";
    private static final String TOPIC2_NAME = "TestTopic2";
    private static final String USER_SESSION_TOKEN = "SessionToken";

    @Mock
    private UsersDaoRepository usersDaoRepositoryMock;
    @Mock
    private TopicService topicServiceMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @InjectMocks
    private UserServiceImpl userService;
    private UserCreateRequestBody userCreateRequestBody;
    private UserLoginRequestBody userLoginRequestBody;
    private User user;
    private UserTopicsRequestBody userTopicsRequestBody;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        userCreateRequestBody = new UserCreateRequestBody();
        userCreateRequestBody.setUsername(USERNAME);
        userCreateRequestBody.setPassword(PASSWORD);

        userLoginRequestBody = new UserLoginRequestBody();
        userLoginRequestBody.setUsername(USERNAME);
        userLoginRequestBody.setPassword(PASSWORD);

        userTopicsRequestBody = new UserTopicsRequestBody();

        user = new User(USERNAME, PASSWORD);
        user.setUserId(1L);

        when(passwordEncoderMock.encode(PASSWORD)).thenReturn("EncodedPassword");
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createNewUser_userExist_notCreated(){
        when(usersDaoRepositoryMock.countUsersByUsername(USERNAME)).thenReturn(1);

        userService.createNewUser(userCreateRequestBody);

        verify(usersDaoRepositoryMock).countUsersByUsername(userCreateRequestBody.getUsername());
    }

    @Test
    public void createNewUser_userDoesntExistNoTopics_createdSuccessfully(){
        userCreateRequestBody.setInterestedTopics(new ArrayList<>());

        when(topicServiceMock.createNewTopicIfNotExists(TOPIC1_NAME.toLowerCase())).thenReturn(new Topic(TOPIC1_NAME));
        when(topicServiceMock.createNewTopicIfNotExists(TOPIC2_NAME.toLowerCase())).thenReturn(new Topic(TOPIC2_NAME));
        when(usersDaoRepositoryMock.countUsersByUsername(USERNAME)).thenReturn(0);
        when(usersDaoRepositoryMock.save(user)).thenReturn(user);

        final ResponseEntity<GenericMessageResponse> newUserResponse = userService.createNewUser(userCreateRequestBody);

        assertEquals("Created successfully. Login to get your token and use your newly created account.", newUserResponse.getBody().getMessage());
        assertEquals(200, newUserResponse.getStatusCodeValue());
        verify(usersDaoRepositoryMock).countUsersByUsername(userCreateRequestBody.getUsername());
        verify(topicServiceMock, times(0)).createNewTopicIfNotExists(anyString());
        verify(passwordEncoderMock).encode(userCreateRequestBody.getPassword());
        verify(usersDaoRepositoryMock).save(any(User.class));
    }

    @Test
    public void createNewUser_userDoesntExist_createdSuccessfully(){
        userCreateRequestBody.setInterestedTopics(Arrays.asList(TOPIC1_NAME, TOPIC2_NAME));

        when(topicServiceMock.createNewTopicIfNotExists(TOPIC1_NAME.toLowerCase())).thenReturn(new Topic(TOPIC1_NAME));
        when(topicServiceMock.createNewTopicIfNotExists(TOPIC2_NAME.toLowerCase())).thenReturn(new Topic(TOPIC2_NAME));
        when(usersDaoRepositoryMock.countUsersByUsername(USERNAME)).thenReturn(0);
        when(usersDaoRepositoryMock.save(user)).thenReturn(user);

        final ResponseEntity<GenericMessageResponse> newUserResponse = userService.createNewUser(userCreateRequestBody);

        assertEquals("Created successfully. Login to get your token and use your newly created account.", newUserResponse.getBody().getMessage());
        assertEquals(200, newUserResponse.getStatusCodeValue());
        verify(usersDaoRepositoryMock).countUsersByUsername(userCreateRequestBody.getUsername());
        verify(topicServiceMock, times(2)).createNewTopicIfNotExists(anyString());
        verify(passwordEncoderMock).encode(userCreateRequestBody.getPassword());
        verify(usersDaoRepositoryMock).save(any(User.class));
    }

    @Test
    public void createNewUser_creatingTopicReturnsNull_createdSuccessfullyButWithoutTopics(){
        userCreateRequestBody.setInterestedTopics(Arrays.asList(TOPIC1_NAME, TOPIC2_NAME));

        when(topicServiceMock.createNewTopicIfNotExists(TOPIC1_NAME.toLowerCase())).thenThrow(new RuntimeException());
        when(topicServiceMock.createNewTopicIfNotExists(TOPIC2_NAME.toLowerCase())).thenThrow(new RuntimeException());
        when(usersDaoRepositoryMock.countUsersByUsername(USERNAME)).thenReturn(0);
        when(usersDaoRepositoryMock.save(user)).thenReturn(user);

        final ResponseEntity<GenericMessageResponse> newUserResponse = userService.createNewUser(userCreateRequestBody);

        assertEquals("Created successfully but one or more topics can not be assigned to the user. Login to get your token and use your newly created account.",
                     newUserResponse.getBody().getMessage());
        assertEquals(202, newUserResponse.getStatusCodeValue());
        verify(usersDaoRepositoryMock).countUsersByUsername(userCreateRequestBody.getUsername());
        verify(topicServiceMock, times(2)).createNewTopicIfNotExists(anyString());
        verify(passwordEncoderMock).encode(userCreateRequestBody.getPassword());
        verify(usersDaoRepositoryMock).save(any(User.class));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void loginAndGenerateToken_userDoesNotExists_invalidCredentialsException(){
        when(usersDaoRepositoryMock.findUsersByUsername(USERNAME)).thenReturn(Optional.empty());

        userService.loginAndGenerateToken(userLoginRequestBody);
        verify(usersDaoRepositoryMock).findUsersByUsername(userLoginRequestBody.getUsername());
    }

    @Test(expected = InvalidCredentialsException.class)
    public void loginAndGenerateToken_passwordIncorrect_invalidCredentialsException(){
        when(usersDaoRepositoryMock.findUsersByUsername(USERNAME)).thenReturn(Optional.of(user));

        when(passwordEncoderMock.matches(eq(user.getPassword()), anyString())).thenReturn(false);

        final ResponseEntity<SessionTokenAndMessageResponse> response = userService.loginAndGenerateToken(userLoginRequestBody);

        verify(usersDaoRepositoryMock).findUsersByUsername(userLoginRequestBody.getUsername());
        verify(passwordEncoderMock).matches(eq(user.getPassword()), anyString());
    }

    @Test
    public void loginAndGenerateToken_sessionTokenNull_loginOk(){
        when(usersDaoRepositoryMock.findUsersByUsername(USERNAME)).thenReturn(Optional.of(user));

        when(passwordEncoderMock.matches(eq(user.getPassword()), anyString())).thenReturn(true);

        final ResponseEntity<SessionTokenAndMessageResponse> response = userService.loginAndGenerateToken(userLoginRequestBody);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged in", response.getBody().getMessage());
        assertNotNull(response.getBody().getSessionToken());
        assertNotNull(user.getSessionToken());
        assertNotNull(user.getSessionTokenCreated());
        verify(usersDaoRepositoryMock).findUsersByUsername(userLoginRequestBody.getUsername());
        verify(passwordEncoderMock).matches(eq(user.getPassword()), anyString());
    }

    @Test
    public void loginAndGenerateToken_sessionTokenCreatedLessThan30Mins_loginOk(){
        final Timestamp sessionTokenCreated = new Timestamp(System.currentTimeMillis());

        user.setSessionToken(USER_SESSION_TOKEN);
        user.setSessionTokenCreated(sessionTokenCreated);

        when(usersDaoRepositoryMock.findUsersByUsername(USERNAME)).thenReturn(Optional.of(user));

        when(passwordEncoderMock.matches(eq(user.getPassword()), anyString())).thenReturn(true);

        final ResponseEntity<SessionTokenAndMessageResponse> response = userService.loginAndGenerateToken(userLoginRequestBody);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged in", response.getBody().getMessage());
        assertEquals(USER_SESSION_TOKEN, response.getBody().getSessionToken());
        assertEquals(USER_SESSION_TOKEN, user.getSessionToken());
        assertEquals(sessionTokenCreated, user.getSessionTokenCreated());
        assertNotNull(user.getSessionTokenLastUsed());
        verify(usersDaoRepositoryMock).findUsersByUsername(userLoginRequestBody.getUsername());
        verify(passwordEncoderMock).matches(eq(user.getPassword()), anyString());
    }

    @Test
    public void loginAndGenerateToken_sessionTokenCreatedLastUsedLessThan30Mins_loginOk(){
        final Timestamp sessionTokenCreated = new Timestamp(System.currentTimeMillis()  - TimeUnit.HOURS.toMillis(1));

        user.setSessionToken(USER_SESSION_TOKEN);
        user.setSessionTokenCreated(sessionTokenCreated);
        user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));

        when(usersDaoRepositoryMock.findUsersByUsername(USERNAME)).thenReturn(Optional.of(user));

        when(passwordEncoderMock.matches(eq(user.getPassword()), anyString())).thenReturn(true);

        final ResponseEntity<SessionTokenAndMessageResponse> response = userService.loginAndGenerateToken(userLoginRequestBody);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged in", response.getBody().getMessage());
        assertEquals(USER_SESSION_TOKEN, response.getBody().getSessionToken());
        assertEquals(USER_SESSION_TOKEN, user.getSessionToken());
        assertEquals(sessionTokenCreated, user.getSessionTokenCreated());
        assertNotNull(user.getSessionTokenLastUsed());
        verify(usersDaoRepositoryMock).findUsersByUsername(userLoginRequestBody.getUsername());
        verify(passwordEncoderMock).matches(eq(user.getPassword()), anyString());
    }

    @Test
    public void loginAndGenerateToken_sessionTokenCreatedLastUsedMoreThan30Mins_loginOk(){
        final Timestamp sessionTokenCreated = new Timestamp(System.currentTimeMillis()  - TimeUnit.HOURS.toMillis(1));
        final Timestamp sessionTokenLastUsed = new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));

        user.setSessionToken(USER_SESSION_TOKEN);
        user.setSessionTokenCreated(sessionTokenCreated);
        user.setSessionTokenLastUsed(sessionTokenLastUsed);

        when(usersDaoRepositoryMock.findUsersByUsername(USERNAME)).thenReturn(Optional.of(user));

        when(passwordEncoderMock.matches(eq(user.getPassword()), anyString())).thenReturn(true);

        final ResponseEntity<SessionTokenAndMessageResponse> response = userService.loginAndGenerateToken(userLoginRequestBody);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged in", response.getBody().getMessage());
        assertNotEquals(USER_SESSION_TOKEN, response.getBody().getSessionToken());
        assertNotEquals(USER_SESSION_TOKEN, user.getSessionToken());
        assertNotEquals(sessionTokenCreated, user.getSessionTokenCreated());
        verify(usersDaoRepositoryMock).findUsersByUsername(userLoginRequestBody.getUsername());
        verify(passwordEncoderMock).matches(eq(user.getPassword()), anyString());
    }

    @Test
    public void logout_sessionTokenValuesPopulated_logoutOk(){
        final Timestamp sessionTokenCreated = new Timestamp(System.currentTimeMillis());
        final Timestamp sessionTokenLastUsed = new Timestamp(System.currentTimeMillis());

        user.setSessionToken(USER_SESSION_TOKEN);
        user.setSessionTokenCreated(sessionTokenCreated);
        user.setSessionTokenLastUsed(sessionTokenLastUsed);

        final ResponseEntity<GenericMessageResponse> response = userService.logout(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully.", response.getBody().getMessage());
        assertNull(user.getSessionToken());
        assertNull(user.getSessionTokenCreated());
        assertNull(user.getSessionTokenLastUsed());
    }
    
    @Test
    public void changeInterestedTopics_noChanges_noTopicChangesApplied(){
        final ResponseEntity<GenericMessageResponse> response = userService.changeInterestedTopics(user,
                                                                                                   userTopicsRequestBody.getAdditions(),
                                                                                                   userTopicsRequestBody.getRemovals());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Topic changes applied.", response.getBody().getMessage());
        assertEquals(0, user.getUserTopics().size());
        verify(topicServiceMock, times(0)).createNewTopicIfNotExists(anyString());
    }

    @Test
    public void changeInterestedTopics_additions_topicChangesApplied(){
        final String testTopic3= "TestTopic3";

        final Set<UserTopicMapping> userTopics = new HashSet<>();
        userTopics.add(new UserTopicMapping(user, new Topic(TOPIC1_NAME), new Timestamp(System.currentTimeMillis())));
        userTopics.add(new UserTopicMapping(user, new Topic(TOPIC2_NAME), new Timestamp(System.currentTimeMillis())));

        user.getUserTopics().addAll(userTopics);

        userTopicsRequestBody.setAdditions(Collections.singletonList(testTopic3));

        when(topicServiceMock.createNewTopicIfNotExists(testTopic3.toLowerCase())).thenReturn(new Topic(testTopic3));

        final ResponseEntity<GenericMessageResponse> response = userService.changeInterestedTopics(user,
                                                                                                   userTopicsRequestBody.getAdditions(),
                                                                                                   userTopicsRequestBody.getRemovals());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Topic changes applied.", response.getBody().getMessage());
        assertEquals(3, user.getUserTopics().size());

        user.getUserTopics().stream()
                            .filter(userTopic -> userTopic.getTopic().getTopicName().equals(testTopic3))
                            .forEach(userTopicMapping -> {
                                assertTrue(userTopicMapping.isEnabled());
                                assertNotNull(userTopicMapping.getInterestedFrom());
                                assertNull(userTopicMapping.getInterestedTo());
                            });

        verify(topicServiceMock).createNewTopicIfNotExists(anyString());
    }

    @Test
    public void changeInterestedTopics_removals_topicChangesApplied(){
        userCreateRequestBody.setInterestedTopics(Arrays.asList(TOPIC1_NAME, TOPIC2_NAME));

        final Set<UserTopicMapping> userTopics = new HashSet<>();
        userTopics.add(new UserTopicMapping(user, new Topic(TOPIC1_NAME.toLowerCase()), new Timestamp(System.currentTimeMillis())));
        userTopics.add(new UserTopicMapping(user, new Topic(TOPIC2_NAME.toLowerCase()), new Timestamp(System.currentTimeMillis())));

        user.getUserTopics().addAll(userTopics);

        userTopicsRequestBody.setRemovals(Collections.singletonList(TOPIC2_NAME));

        final ResponseEntity<GenericMessageResponse> response = userService.changeInterestedTopics(user,
                                                                                                   userTopicsRequestBody.getAdditions(),
                                                                                                   userTopicsRequestBody.getRemovals());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Topic changes applied.", response.getBody().getMessage());
        assertEquals(2, user.getUserTopics().size());

        user.getUserTopics().stream()
                            .filter(userTopicMapping -> userTopicMapping.getTopic().getTopicName().equals(TOPIC1_NAME))
                            .forEach(userTopicMapping -> {
                                assertFalse(userTopicMapping.isEnabled());
                                assertNotNull(userTopicMapping.getInterestedTo());
                            });

        verify(topicServiceMock, times(0)).createNewTopicIfNotExists(anyString());
    }

    @Test(expected = RuntimeException.class)
    public void changeInterestedTopics_topicCreationFails_errorResponse(){
        final String testTopic3= "TestTopic3";

        final Set<UserTopicMapping> userTopics = new HashSet<>();
        userTopics.add(new UserTopicMapping(user, new Topic(TOPIC1_NAME), new Timestamp(System.currentTimeMillis())));
        userTopics.add(new UserTopicMapping(user, new Topic(TOPIC2_NAME), new Timestamp(System.currentTimeMillis())));

        user.getUserTopics().addAll(userTopics);

        userTopicsRequestBody.setAdditions(Collections.singletonList(testTopic3));

        when(topicServiceMock.createNewTopicIfNotExists(testTopic3.toLowerCase())).thenThrow(new RuntimeException());

        final ResponseEntity<GenericMessageResponse> response = userService.changeInterestedTopics(user,
                                                                                                   userTopicsRequestBody.getAdditions(),
                                                                                                   userTopicsRequestBody.getRemovals());

        verify(topicServiceMock).createNewTopicIfNotExists(anyString());
    }

    @Test
    public void changeInterestedTopics_removalOfNotInterestedTopic_topicChangesApplied(){
        userCreateRequestBody.setInterestedTopics(Arrays.asList(TOPIC1_NAME, TOPIC2_NAME));
        final Topic topic1 = new Topic(TOPIC1_NAME.toLowerCase());
        final Topic topic2 = new Topic(TOPIC2_NAME.toLowerCase());
        final UserTopicMapping userTopicMapping1 = new UserTopicMapping(user, topic1, new Timestamp(System.currentTimeMillis()));
        final UserTopicMapping userTopicMapping2 = new UserTopicMapping(user, topic2, new Timestamp(System.currentTimeMillis()));

        final Set<UserTopicMapping> userTopics = new HashSet<>();
        userTopics.add(userTopicMapping1);
        userTopics.add(userTopicMapping2);

        user.getUserTopics().addAll(userTopics);

        userTopicsRequestBody.setAdditions(new ArrayList<>());
        userTopicsRequestBody.setRemovals(Collections.singletonList("TestTopic3"));

        final ResponseEntity<GenericMessageResponse> response = userService.changeInterestedTopics(user,
                                                                                                   userTopicsRequestBody.getAdditions(),
                                                                                                   userTopicsRequestBody.getRemovals());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Topic changes applied.", response.getBody().getMessage());
        assertEquals(2, user.getUserTopics().size());
        assertTrue(user.getUserTopics().contains(userTopicMapping1));
        assertTrue(user.getUserTopics().contains(userTopicMapping2));
        verify(topicServiceMock, times(0)).createNewTopicIfNotExists(anyString());
    }

    @Test
    public void getAllUsers_userDaoCalled_getOk(){
        when(usersDaoRepositoryMock.findAll()).thenReturn(Arrays.asList(user, new User("Username2", "Password2")));

        userService.getAllUsers();

        verify(usersDaoRepositoryMock).findAll();
    }

    @Test(expected = InvalidCredentialsException.class)
    public void getUserFromDbUsingSessionToken_userDoesNotExist_invalidCredentialsException(){
        when(usersDaoRepositoryMock.findUsersBySessionToken(USER_SESSION_TOKEN)).thenReturn(Optional.empty());

        userService.getUserFromDbUsingSessionToken(USER_SESSION_TOKEN);

        verify(usersDaoRepositoryMock).findUsersBySessionToken(USER_SESSION_TOKEN);
    }

    @Test
    public void getUserFromDbUsingSessionToken_userExists_userRetrieved(){
        when(usersDaoRepositoryMock.findUsersBySessionToken(USER_SESSION_TOKEN)).thenReturn(Optional.of(user));

        final User userFromDbUsingSessionToken = userService.getUserFromDbUsingSessionToken(USER_SESSION_TOKEN);

        assertEquals(userFromDbUsingSessionToken.getUsername(), USERNAME);
        assertEquals(userFromDbUsingSessionToken.getPassword(), PASSWORD);
        verify(usersDaoRepositoryMock).findUsersBySessionToken(USER_SESSION_TOKEN);
    }

    @Test
    public void invalidateInactiveSessionTokens_noTokensInDb_noTokensNulled(){
        when(usersDaoRepositoryMock.streamUsersBySessionTokenNotNull()).thenReturn(Stream.empty());

        userService.invalidateInactiveSessionTokens();

        verify(usersDaoRepositoryMock).streamUsersBySessionTokenNotNull();
    }

    @Test
    public void invalidateInactiveSessionTokens_noInactiveSessionTokens_noTokensNulled(){
        user.setSessionToken(USER_SESSION_TOKEN);
        user.setSessionTokenCreated(new Timestamp(System.currentTimeMillis()));
        user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis()));

        final List<User> users = Collections.singletonList(user);

        when(usersDaoRepositoryMock.streamUsersBySessionTokenNotNull()).thenReturn(users.stream());

        userService.invalidateInactiveSessionTokens();

        assertNotNull(user.getSessionToken());
        assertNotNull(user.getSessionTokenCreated());
        assertNotNull(user.getSessionTokenLastUsed());
        verify(usersDaoRepositoryMock).streamUsersBySessionTokenNotNull();
    }

    @Test
    public void invalidateInactiveSessionTokens_inactiveSessionTokens_noTokensNulled(){
        user.setSessionToken(USER_SESSION_TOKEN);
        user.setSessionTokenCreated(new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));
        user.setSessionTokenLastUsed(new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));

        final List<User> users = Collections.singletonList(user);

        when(usersDaoRepositoryMock.streamUsersBySessionTokenNotNull()).thenReturn(users.stream());

        userService.invalidateInactiveSessionTokens();

        assertNull(user.getSessionToken());
        assertNull(user.getSessionTokenCreated());
        assertNull(user.getSessionTokenLastUsed());
        verify(usersDaoRepositoryMock).streamUsersBySessionTokenNotNull();
    }

    @Test
    public void updateSessionTokenLastUsed_sessionTokenLastUsedNull_sessionTokenLastUsedNotNull(){
        userService.updateSessionTokenLastUsed(user);

        assertNotNull(user.getSessionTokenLastUsed());
    }
}
