package com.uom.las3014.service;

import com.uom.las3014.api.response.TopicsTopStoryResponse;
import com.uom.las3014.dao.Story;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface StoriesService {
    Story createNewOrUpdateExistingStory(Story story);

//    List<Story> getLastWeeksUndeletedTopics();
//
//    List<Story> get12HrsUndeletedTopics();

    List<Story> getUndeletedTopicsBetween(Timestamp createdAfter, Timestamp createdBefore);

//    Optional<Story> getTopStoryContainingKeyword(String keyword);

    Optional<Story> getTopStoryContainingKeywordAndCreatedInLastWeek(String keyword);

    ResponseEntity<TopicsTopStoryResponse> getTopStoryForTopics(String sessionToken);
}
