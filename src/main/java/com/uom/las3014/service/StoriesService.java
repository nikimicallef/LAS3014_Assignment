package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.dao.Story;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface StoriesService {
    List<Story> getUndeletedTopicsAfterTimestamp(Timestamp createdAfter);

    List<Story> getUndeletedStoriesContainingKeywordAndAfterTimestamp(String keyword, Timestamp createdAfter);

    ResponseEntity<GroupTopStoriesByDateResponse> getTopStoryForTopics(String sessionToken);

    void saveAllStories(Iterable<? extends Story> stories);

    List<Story> getTop3UndeletedStoriesAfterTimestamp(final Timestamp dateAfter);

    void deleteByDateCreatedBeforeAndDigestsEmpty(Timestamp timestamp);
}
