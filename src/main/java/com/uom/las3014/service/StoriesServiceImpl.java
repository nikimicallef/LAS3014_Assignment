package com.uom.las3014.service;

import com.uom.las3014.api.response.TopStoryResponse;
import com.uom.las3014.api.response.TopicTopStoryResponse;
import com.uom.las3014.api.response.TopicsTopStoryResponse;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.StoriesDaoRepository;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class StoriesServiceImpl implements StoriesService{
    @Autowired
    private StoriesDaoRepository storiesDaoRepository;

    @Autowired
    private UserService userService;

    @Override
    public Story createNewOrUpdateExistingStory(final Story story) {
        return storiesDaoRepository.save(story);
    }

    @Override
    public List<Story> getLastWeeksUndeletedTopics() {
        return storiesDaoRepository.findAllByDateCreatedIsBetweenAndDeletedIs(new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)), new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)), false);
    }

    @Override
    public List<Story> get12HrsUndeletedTopics() {
        return storiesDaoRepository.findAllByDateCreatedIsBetweenAndDeletedIs(new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)), new Timestamp(System.currentTimeMillis()), false);
    }

    @Override
    public Optional<Story> getTopStoryContainingKeyword(final String keyword) {
        return storiesDaoRepository.findTop1ByTitleContainingOrderByScoreDesc(keyword);
    }

    @Override
    public ResponseEntity<TopicsTopStoryResponse> getTopStoryForTopics(final String sessionToken){
        final Optional<User> user = userService.getUserFromDbUsingSessionToken(sessionToken);

        final User retrievedUser = user.orElseThrow(InvalidCredentialsException::new);

        final List<TopicTopStoryResponse> topicTopStoryResponses = retrievedUser.getUserTopics().stream().map(userTopicMapping -> {
            if(userTopicMapping.getTopic().getTopStoryId() != null) {
                final String topStoryTitle = userTopicMapping.getTopic().getTopStoryId().getTitle();
                final String topStoryUrl = userTopicMapping.getTopic().getTopStoryId().getUrl();
                final TopStoryResponse topStoryResponse = new TopStoryResponse(topStoryTitle, topStoryUrl);
                return new TopicTopStoryResponse(userTopicMapping.getTopic().getTopicName(), topStoryResponse);
            } else {
                return new TopicTopStoryResponse(userTopicMapping.getTopic().getTopicName(), null);
            }
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new TopicsTopStoryResponse(topicTopStoryResponses));
    }
}
