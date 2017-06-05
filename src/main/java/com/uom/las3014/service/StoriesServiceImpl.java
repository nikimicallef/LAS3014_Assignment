package com.uom.las3014.service;

import com.google.common.collect.Ordering;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse.TopStoryResponse;
import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.StoriesDaoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    @Override
    public List<Story> getUndeletedTopicsAfterTimestamp(final Timestamp createdAfter) {
        return storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalse(createdAfter);
    }

    @Override
    public List<Story> getUndeletedStoriesContainingKeywordAndAfterTimestamp(String keyword, Timestamp createdAfter) {
        return storiesDaoRepository.findAllByTitleContainingAndDateCreatedIsAfterAndDeletedIsFalse(keyword, createdAfter);
    }

    @Override
    @Cacheable(MyCacheManager.TOPIC_CACHE)
    public ResponseEntity<GroupTopStoriesByDateResponse> getTopStoryForTopics(final User user){
        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(LocalDate.now());

        user.getUserTopics().forEach(userTopicMapping -> {
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

    @Override
    public void saveAllStories(final Iterable<? extends Story> stories) {
        storiesDaoRepository.save(stories);
    }

    @Override
    public List<Story> getTop3UndeletedStoriesAfterTimestamp(final Timestamp dateAfter) {
        final List<Story> stories = storiesDaoRepository.findAllByDateCreatedIsAfterAndDeletedIsFalseAndScoreGreaterThan(dateAfter, 5);

        return Ordering.from(Story::compareTo).greatestOf(stories, 3);
    }

    @Override
    public void deleteByDateCreatedBeforeAndDigestsEmpty(Timestamp timestamp) {
        storiesDaoRepository.deleteByDateCreatedBeforeAndDigestsEmpty(timestamp);
    }
}
