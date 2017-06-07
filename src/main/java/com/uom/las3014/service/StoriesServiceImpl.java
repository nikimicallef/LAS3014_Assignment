package com.uom.las3014.service;

import com.google.common.collect.Ordering;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse.TopStoryResponse;
import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.StoriesDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class StoriesServiceImpl implements StoriesService{
    @Autowired
    private StoriesDaoRepository storiesDaoRepository;

    /**
     * @param createdAfter Only {@link Story} with {@link Story#deleted} false and {@link Story#dateCreated} after are retrieved
     * @return {@link Story} which satisfy the condition
     */
    @Override
    public List<Story> getUndeletedTopicsAfterTimestamp(final Timestamp createdAfter) {
        return storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalse(createdAfter);
    }

    /**
     * @param keyword {@link Story#title} must contain keyword
     * @param createdAfter Only {@link Story} with {@link Story#deleted} false and {@link Story#dateCreated} after are retrieved
     * @return {@link Story} which satisfy the condition
     */
    @Override
    public List<Story> getUndeletedStoriesContainingKeywordAndAfterTimestamp(final String keyword, final Timestamp createdAfter) {
        return storiesDaoRepository.findAllByTitleContainingAndDateCreatedIsAfterAndDeletedIsFalse(keyword, createdAfter);
    }

    /**
     * @param user Consider only {@link Story} where the {@link User} is interested in a {@link Topic} which is in the {@link Story#title}
     * @return {@link Story} which satisfy the condition
     */
    @Override
    @Cacheable(MyCacheManager.TOP_STORY_CACHE)
    public ResponseEntity<GroupTopStoriesByDateResponse> getTopStoryForTopics(final User user){
        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(LocalDate.now());

        user.getUserTopics().stream()
                .filter(UserTopicMapping::isEnabled)
                .forEach(userTopicMapping -> {
                    final TopStoriesForTopicResponse topStoriesForTopicResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(userTopicMapping.getTopic().getTopicName());

                    if(userTopicMapping.getTopic().getTopStoryId() != null) {
                        final String topStoryTitle = userTopicMapping.getTopic().getTopStoryId().getTitle();
                        final String topStoryUrl = userTopicMapping.getTopic().getTopStoryId().getUrl();
                        final Integer topStoryScore = userTopicMapping.getTopic().getTopStoryId().getScore();

                        final TopStoryResponse topStoryResponse = topStoriesForTopicResponse.new TopStoryResponse(topStoryTitle, topStoryUrl, topStoryScore);

                        topStoriesForTopicResponse.getTopStories().add(topStoryResponse);
                    }

                    groupTopStoriesByDateResponse.getTopics().add(topStoriesForTopicResponse);
                });

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupTopStoriesByDateResponse);
    }

    /**
     * @param stories {@link Story} to save
     */
    @Override
    public void saveAllStories(final Iterable<? extends Story> stories) {
        storiesDaoRepository.save(stories);
    }

    /**
     * @param dateAfter Top 3 {@link Story} with the highest {@link Story#score}, {@link Story#deleted} is false and
     *                  {@link Story#dateCreated} is after dateAfter
     * @return {@link Story} which satisfy the condition
     */
    @Override
    public List<Story> getTop3UndeletedStoriesAfterTimestamp(final Timestamp dateAfter) {
        final List<Story> stories = storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(dateAfter, 5);

        return Ordering.from(Story::compareTo).greatestOf(stories, 3);
    }

    /**
     * @param createdBefore Delete all {@link Story} where the {@link Story#dateCreated} is after
     */
    @Override
    public void deleteByDateCreatedBeforeAndDigestsEmpty(final Timestamp createdBefore) {
        storiesDaoRepository.deleteByDateCreatedBeforeAndDigestsEmpty(createdBefore);
    }
}
