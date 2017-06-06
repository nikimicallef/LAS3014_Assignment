package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.UserTopicMappingDaoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class UserTopicMappingServiceImplUnitTests {
    private static final String USERNAME = "TestUser";
    private static final String PASSWORD = "TestPassword";
    private final static String TOPIC1_NAME = "TestTopic1";

    @Mock
    private UserTopicMappingDaoRepository userTopicMappingDaoRepositoryMock;
    @InjectMocks
    private UserTopicMappingServiceImpl userTopicMappingService;
    private Topic topic;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        topic = new Topic(TOPIC1_NAME);
    }

    @Test
    public void findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter_topicAndTimestampNotNull_retrievedOk(){
        when(userTopicMappingDaoRepositoryMock
                .findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(eq(topic), any(Timestamp.class)))
                .thenReturn(Collections.singletonList(
                        new UserTopicMapping(new User(USERNAME, PASSWORD), topic, new Timestamp(System.currentTimeMillis()))));

        final List<UserTopicMapping> userTopicMappingList = userTopicMappingService.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(topic, new Timestamp(System.currentTimeMillis()));


        assertEquals(1, userTopicMappingList.size());
        verify(userTopicMappingDaoRepositoryMock, times(1)).findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(eq(topic), any(Timestamp.class));
    }

    @Test
    public void findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter_noTopicsRetrieved_retrievedOk(){
        when(userTopicMappingDaoRepositoryMock
                .findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(any(Topic.class), any(Timestamp.class)))
                .thenReturn(new ArrayList<>());

        final List<UserTopicMapping> userTopicMappingList = userTopicMappingService.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(topic, new Timestamp(System.currentTimeMillis()));

        assertEquals(0, userTopicMappingList.size());
        verify(userTopicMappingDaoRepositoryMock, times(1)).findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfter(eq(topic), any(Timestamp.class));
    }
}
