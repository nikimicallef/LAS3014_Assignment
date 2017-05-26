package com.uom.las3014.service;

import com.uom.las3014.api.response.TopicsTopStoryResponse;
import com.uom.las3014.dao.Story;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StoriesService {
    Story createNewOrUpdateExistingStory(Story story);

    List<Story> getLastWeeksUndeletedTopics();

    List<Story> get12HrsUndeletedTopics();

    Optional<Story> getTopStoryContainingKeyword(String keyword);

    ResponseEntity<TopicsTopStoryResponse> getTopStoryForTopics(String sessionToken);
}
