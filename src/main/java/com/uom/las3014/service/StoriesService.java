package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.StoriesDaoRepository;
import org.springframework.http.ResponseEntity;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.List;

/**
 * Service for the {@link Story} {@link Entity}.This is the only place which interacts with the {@link StoriesDaoRepository}
 */
public interface StoriesService {
    /**
     * @param createdAfter Only {@link Story} with {@link Story#deleted} false and {@link Story#dateCreated} after are retrieved
     * @return {@link Story} which satisfy the condition
     */
    List<Story> getUndeletedTopicsAfterTimestamp(Timestamp createdAfter);

    /**
     * @param keyword {@link Story#title} must contain keyword
     * @param createdAfter Only {@link Story} with {@link Story#deleted} false and {@link Story#dateCreated} after are retrieved
     * @return {@link Story} which satisfy the condition
     */
    List<Story> getUndeletedStoriesContainingKeywordAndAfterTimestamp(String keyword, Timestamp createdAfter);

    /**
     * @param user Consider only {@link Story} where the {@link User} is interested in a {@link Topic} which is in the {@link Story#title}
     * @return {@link Story} which satisfy the condition
     */
    ResponseEntity<GroupTopStoriesByDateResponse> getTopStoryForTopics(User user);

    /**
     * @param stories {@link Story} to save
     */
    void saveAllStories(Iterable<? extends Story> stories);

    /**
     * @param dateAfter Top 3 {@link Story} with the highest {@link Story#score}, {@link Story#deleted} is false and
     *                  {@link Story#dateCreated} is after dateAfter
     * @return {@link Story} which satisfy the condition
     */
    List<Story> getTop3UndeletedStoriesAfterTimestamp(Timestamp dateAfter);

    /**
     * @param createdBefore Delete all {@link Story} where the {@link Story#dateCreated} is after
     */
    void deleteByDateCreatedBeforeAndDigestsEmpty(Timestamp createdBefore);
}
