package com.uom.las3014.restcontroller;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.MultipleTopStoriesPerDateResponse;
import com.uom.las3014.dao.User;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.service.DigestsService;
import com.uom.las3014.service.StoriesService;
import com.uom.las3014.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class StoriesControllerUnitTests {
    private static final String SESSION_TOKEN = "SessionToken";

    @Mock
    private StoriesService storiesServiceMock;
    @Mock
    private DigestsService digestsServiceMock;
    @Mock
    private UserService userService;
    @InjectMocks
    private StoriesController storiesController;
    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User("Username", "Password");

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(LocalDate.now());

        final ResponseEntity<GroupTopStoriesByDateResponse> topStoryPerDateResponseEntity = ResponseEntity.status(HttpStatus.OK)
                                                                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                                                                .body(groupTopStoriesByDateResponse);

        when(storiesServiceMock.getTopStoryForTopics(any(User.class))).thenReturn(topStoryPerDateResponseEntity);
        when(digestsServiceMock.getLatestWeeklyDigest(any(User.class))).thenReturn(topStoryPerDateResponseEntity);

        final MultipleTopStoriesPerDateResponse multipleTopStoriesPerDateResponse = new MultipleTopStoriesPerDateResponse();

        final ResponseEntity<MultipleTopStoriesPerDateResponse> topStoriesPerDateResponseEntity = ResponseEntity.status(HttpStatus.OK)
                                                                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                                                                .body(multipleTopStoriesPerDateResponse);

        when(digestsServiceMock.getGroupOfWeeklyDigests(any(User.class), any(Date.class), any(Date.class))).thenReturn(topStoriesPerDateResponseEntity);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void getTopStory_userWithSessionTokenDoesNotExist_invalidCredentialsException(){
        when(userService.getUserFromDbUsingSessionToken(anyString())).thenThrow(new InvalidCredentialsException());

        final ResponseEntity<GroupTopStoriesByDateResponse> topStory = storiesController.getTopStory(SESSION_TOKEN);

        verify(userService).getUserFromDbUsingSessionToken(anyString());
        verify(storiesServiceMock, times(0)).getTopStoryForTopics(any(User.class));
    }

    @Test
    public void getTopStory_userWithSessionTokenExists_responseOk(){
        when(userService.getUserFromDbUsingSessionToken(anyString())).thenReturn(user);

        final ResponseEntity<GroupTopStoriesByDateResponse> topStory = storiesController.getTopStory(SESSION_TOKEN);

        verify(userService).getUserFromDbUsingSessionToken(anyString());
        verify(storiesServiceMock).getTopStoryForTopics(any(User.class));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void getLatestDigest_userWithSessionTokenDoesNotExist_invalidCredentialsException(){
        when(userService.getUserFromDbUsingSessionToken(anyString())).thenThrow(new InvalidCredentialsException());

        final ResponseEntity<GroupTopStoriesByDateResponse> latestDigest = storiesController.getLatestDigest(SESSION_TOKEN);

        verify(userService).getUserFromDbUsingSessionToken(anyString());
        verify(digestsServiceMock, times(0)).getLatestWeeklyDigest(any(User.class));
    }

    @Test
    public void getLatestDigest_userWithSessionTokenExists_returnOk(){
        when(userService.getUserFromDbUsingSessionToken(anyString())).thenReturn(user);

        final ResponseEntity<GroupTopStoriesByDateResponse> latestDigest = storiesController.getLatestDigest(SESSION_TOKEN);

        verify(userService).getUserFromDbUsingSessionToken(anyString());
        verify(digestsServiceMock).getLatestWeeklyDigest(any(User.class));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void getDigestsGroup_userWithSessionTokenDoesNotExist_invalidCredentialsException(){
        when(userService.getUserFromDbUsingSessionToken(anyString())).thenThrow(new InvalidCredentialsException());

        final ResponseEntity<MultipleTopStoriesPerDateResponse> digestsGroup = storiesController.getDigestsGroup(SESSION_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

        verify(userService).getUserFromDbUsingSessionToken(anyString());
        verify(digestsServiceMock, times(0)).getGroupOfWeeklyDigests(any(User.class), any(Date.class), any(Date.class));
    }

    @Test
    public void getDigestsGroup_userWithSessionTokenExists_responseOk(){
        when(userService.getUserFromDbUsingSessionToken(anyString())).thenReturn(user);

        final ResponseEntity<MultipleTopStoriesPerDateResponse> digestsGroup = storiesController.getDigestsGroup(SESSION_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

        verify(userService).getUserFromDbUsingSessionToken(anyString());
        verify(digestsServiceMock).getGroupOfWeeklyDigests(any(User.class), any(Date.class), any(Date.class));
    }
}
