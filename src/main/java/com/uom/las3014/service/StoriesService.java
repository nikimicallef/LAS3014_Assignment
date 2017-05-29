package com.uom.las3014.service;

import com.uom.las3014.api.response.TopicsTopStoryResponse;
import com.uom.las3014.dao.Story;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface StoriesService {
    List<Story> getUndeletedTopicsAfterTimestamp(Timestamp createdAfter);

    List<Story> getUndeletedStoriesContainingKeywordAndAfterTimestamp(String keyword, Timestamp createdAfter);

    ResponseEntity<TopicsTopStoryResponse> getTopStoryForTopics(String sessionToken);

    void saveAllStories(Iterable<? extends Story> stories);
}
