package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse.TopStoryResponse;
import com.uom.las3014.api.response.MultipleTopStoriesPerDateResponse;
import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.UserTopicMapping;
import com.uom.las3014.dao.springdata.DigestDaoRepository;
import com.uom.las3014.exceptions.InvalidDateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DigestsServiceImpl implements DigestsService {
    @Autowired
    private DigestDaoRepository digestDaoRepository;

    @Override
    public void saveAll(Iterable<? extends Digest> digests) {
        digestDaoRepository.save(digests);
    }

    @Override
    public void deleteDigestByDayOfWeekBefore(Timestamp timestamp) {
        digestDaoRepository.deleteDigestByDayOfWeekBefore(new java.sql.Date(timestamp.getTime()));
    }

    @Override
    @Cacheable(MyCacheManager.DIGESTS_CACHE)
    public ResponseEntity<GroupTopStoriesByDateResponse> getLatestWeeklyDigest(final User user) {
        final Set<Digest> digests = digestDaoRepository.findLatestDigestsForUser(user);

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse;

        if(digests.size() == 0){
            groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse();
        } else {
            final java.sql.Date dayOfWeek = digests.iterator().next().getDayOfWeek() ;

            groupTopStoriesByDateResponse = createDigestResponse(user.getUserTopics(), digests, dayOfWeek);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupTopStoriesByDateResponse);
    }

    //This method has not been cached since the cache will grow quickly since a small deviation in one of the dates will create a new entry.
    @Override
    public ResponseEntity<MultipleTopStoriesPerDateResponse> getGroupOfWeeklyDigests(User user, Date dateFrom, Date dateTo){
        if(dateFrom.after(dateTo)){
            throw new InvalidDateException("To date must be after the from date.");
        }

        final Set<Digest> digests = digestDaoRepository.findGroupOfDigestsBetweenDatesForUser(new java.sql.Date(dateFrom.getTime()), new java.sql.Date(dateTo.getTime()), user);

        final Map<java.sql.Date, Set<Digest>> digestsGroupedByDate = groupDigestsByDate(digests);

        final List<GroupTopStoriesByDateResponse> topStoriesByDateResponse = groupOfDigests(user, digestsGroupedByDate);

        final MultipleTopStoriesPerDateResponse multipleTopStoriesPerDateResponse = new MultipleTopStoriesPerDateResponse(topStoriesByDateResponse );

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(multipleTopStoriesPerDateResponse);
    }

    private List<GroupTopStoriesByDateResponse> groupOfDigests(User user, Map<java.sql.Date, Set<Digest>> digestsGroupedByDate) {
        return digestsGroupedByDate.keySet().stream()
                                        .map(date -> createDigestResponse(user.getUserTopics(), digestsGroupedByDate.get(date), date))
                                        .collect(Collectors.toList());
    }

    private GroupTopStoriesByDateResponse createDigestResponse(Set<UserTopicMapping> userTopicMappings, Set<Digest> digests, java.sql.Date dayOfWeek) {
        final Map<Topic, Set<Digest>> digestsPerTopic = groupDigestsByTopic(digests);

        return createDigestForDay(userTopicMappings, dayOfWeek, digestsPerTopic);
    }

    private Map<java.sql.Date, Set<Digest>> groupDigestsByDate(final Set<Digest> digests) {
        final Map<java.sql.Date, Set<Digest>> digestsPerTopic = new HashMap<>();

        digests.forEach(digest -> {
            if (digestsPerTopic.containsKey(digest.getDayOfWeek())) {
                digestsPerTopic.get(digest.getDayOfWeek()).add(digest);
            } else {
                final Set<Digest> digestsForTopic = new HashSet<>();
                digestsForTopic.add(digest);
                digestsPerTopic.put(digest.getDayOfWeek(), digestsForTopic);
            }
        });

        return digestsPerTopic;
    }

    private Map<Topic, Set<Digest>> groupDigestsByTopic(final Set<Digest> digests) {
        final Map<Topic, Set<Digest>> digestsPerTopic = new HashMap<>();

        digests.forEach(digest -> {
            if (digestsPerTopic.containsKey(digest.getTopicId())) {
                digestsPerTopic.get(digest.getTopicId()).add(digest);
            } else {
                final Set<Digest> digestsForTopic = new HashSet<>();
                digestsForTopic.add(digest);
                digestsPerTopic.put(digest.getTopicId(), digestsForTopic);
            }
        });

        return digestsPerTopic;
    }

    private GroupTopStoriesByDateResponse createDigestForDay(final Set<UserTopicMapping> userTopicMappings,
                                                             final java.sql.Date dayOfWeek,
                                                             final Map<Topic, Set<Digest>> digestsPerTopic) {
        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(dayOfWeek.toLocalDate());

        digestsPerTopic.keySet().forEach(topic -> {
            final String topicName = ((topic == null) ? "weekly" : topic.getTopicName());

            final TopStoriesForTopicResponse topStoriesForTopicResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(topicName);

            digestsPerTopic.get(topic).forEach(digest -> {
                final String storyTitle = digest.getStoryId().getTitle();
                final String storyUrl = digest.getStoryId().getUrl();
                final Integer storyScore = digest.getStoryId().getScore();

                final TopStoryResponse topStoryResponse = topStoriesForTopicResponse.new TopStoryResponse(storyTitle, storyUrl, storyScore);

                topStoriesForTopicResponse.getTopStories().add(topStoryResponse);
            });

            groupTopStoriesByDateResponse.getTopics().add(topStoriesForTopicResponse);
        });

        addEntriesForTopicsWithoutDigests(userTopicMappings, digestsPerTopic, groupTopStoriesByDateResponse);

        return groupTopStoriesByDateResponse;
    }

    private void addEntriesForTopicsWithoutDigests(final Set<UserTopicMapping> userTopicMappings,
                                                   final Map<Topic, Set<Digest>> digestsPerTopic,
                                                   final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse) {
        userTopicMappings.stream()
                .filter(userTopicMapping -> !digestsPerTopic.keySet().contains(userTopicMapping.getTopic()))
                .forEach(userTopicMapping -> {
                    groupTopStoriesByDateResponse.getTopics().add(groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(userTopicMapping.getTopic().getTopicName()));
                });
    }
}
