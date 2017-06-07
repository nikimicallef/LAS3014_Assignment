package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse.TopStoriesForTopicResponse.TopStoryResponse;
import com.uom.las3014.api.response.MultipleTopStoriesPerDateResponse;
import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.DigestDaoRepository;
import com.uom.las3014.exceptions.InvalidDateException;
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

    /**
     * {@inheritDoc}
     * @param digestsToSave {@link Iterable} of {@link Digest} to persist
     */
    @Override
    public void saveAll(final Iterable<? extends Digest> digestsToSave) {
        digestDaoRepository.save(digestsToSave);
    }

    /**
     * {@inheritDoc}
     * @param dayOfWeekBefore Deletes all {@link Digest} where {@link Digest#dayOfWeek} is before this param
     */
    @Override
    public void deleteDigestByDayOfWeekBefore(final Timestamp dayOfWeekBefore) {
        digestDaoRepository.deleteDigestByDayOfWeekBefore(new java.sql.Date(dayOfWeekBefore.getTime()));
    }

    /**
     * @param user Gets latest weekly {@link Digest} assigned to {@link User}
     * @return Top {@link Story} for each {@link Topic} for a specific date
     */
    @Override
    @Cacheable(MyCacheManager.DIGESTS_CACHE)
    public ResponseEntity<GroupTopStoriesByDateResponse> getLatestWeeklyDigest(final User user) {
        final Set<Digest> digests = digestDaoRepository.findLatestDigestsForUser(user);

        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse;

        if(digests.size() == 0){
            groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse();
        } else {
            final java.sql.Date dayOfWeek = digests.iterator().next().getDayOfWeek() ;

            groupTopStoriesByDateResponse = createDigestResponse(digests, dayOfWeek);
        }
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(groupTopStoriesByDateResponse);
    }

    //This method has not been cached since the cache will grow quickly since a small deviation in one of the dates will create a new entry.
    /**
     * @param user Gets {@link Digest} assigned to this {@link User}
     * @param dateFrom Gets {@link Digest} where {@link Digest#dayOfWeek} is after dateFrom
     * @param dateTo Gets {@link Digest} where {@link Digest#dayOfWeek} is after dateTo
     * @return Top {@link Story} for each {@link Topic} for multiple dates
     */
    @Override
    public ResponseEntity<MultipleTopStoriesPerDateResponse> getGroupOfWeeklyDigests(final User user,
                                                                                     final Date dateFrom,
                                                                                     final Date dateTo){
        if(dateFrom.after(dateTo)){
            throw new InvalidDateException("To date must be after the from date.");
        }

        final java.sql.Date queryDateFrom = new java.sql.Date(dateFrom.getTime());
        final java.sql.Date queryDateTo = new java.sql.Date(dateTo.getTime());
        final Set<Digest> digests = digestDaoRepository.findGroupOfDigestsBetweenDatesForUser(queryDateFrom,
                                                                                              queryDateTo,
                                                                                              user);

        final Map<java.sql.Date, Set<Digest>> digestsGroupedByDate = groupDigestsByDate(digests);

        final List<GroupTopStoriesByDateResponse> topStoriesByDateResponse = groupOfDigests(user, digestsGroupedByDate);

        final MultipleTopStoriesPerDateResponse multipleTopStoriesPerDateResponse = new MultipleTopStoriesPerDateResponse(topStoriesByDateResponse);

        return ResponseEntity.status(HttpStatus.OK)
                              .contentType(MediaType.APPLICATION_JSON)
                              .body(multipleTopStoriesPerDateResponse);
    }

    /**
     * @param digests {@link Digest} to divide depending on their {@link java.sql.Date}
     * @return {@link Digest} divided for different {@link java.sql.Date} in a {@link Map}
     */
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

    /**
     * @param user Handles only for {@link Digest} of this {@link User}
     * @param digestsGroupedByDate {@link Digest} grouped by {@link java.sql.Date} in a {@link Map}
     * @return {@link List} of {@link Digest} per {@link Topic} per {@link java.sql.Date}
     */
    private List<GroupTopStoriesByDateResponse> groupOfDigests(final User user,
                                                               final Map<java.sql.Date, Set<Digest>> digestsGroupedByDate) {
        return digestsGroupedByDate.keySet().stream()
                                        .map(date -> {
                                            final Set<Digest> digests = digestsGroupedByDate.get(date);
                                            return createDigestResponse(digests, date);
                                        })
                                        .collect(Collectors.toList());
    }

    /**
     * From the {@link Digest} in the database a model is created for one date
     * @param digests Different {@link Digest} attributed to the {@link User}
     * @param dayOfWeek {@link java.sql.Date} for which the {@link Digest} is created
     * @return {@link Digest} per {@link Topic} for a specific {@link java.sql.Date} wrapped in a model
     */
    private GroupTopStoriesByDateResponse createDigestResponse(final Set<Digest> digests,
                                                               final java.sql.Date dayOfWeek) {
        final Map<Topic, Set<Digest>> digestsPerTopic = groupDigestsByTopic(digests);

        return createDigestForDay(dayOfWeek, digestsPerTopic);
    }

    /**
     * @param digests {@link Digest} to classify by {@link java.sql.Date}
     * @return {@link Digest} classified by {@link java.sql.Date} in a {@link Map}
     */
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

    /**
     * Creates a {@link Digest} model for a specific {@link java.sql.Date}
     * @param dayOfWeek {@link java.sql.Date} of the {@link Digest} to be created
     * @param digestsPerTopic {@link Digest} divided by {@link Topic} in a {@link Map}
     * @return Model for all top {@link Story} per {@link Topic} on a specific {@link java.sql.Date}
     */
    private GroupTopStoriesByDateResponse createDigestForDay(final java.sql.Date dayOfWeek,
                                                             final Map<Topic, Set<Digest>> digestsPerTopic) {
        final GroupTopStoriesByDateResponse groupTopStoriesByDateResponse = new GroupTopStoriesByDateResponse(dayOfWeek.toLocalDate());

        digestsPerTopic.keySet().forEach(topic -> {
            final String topicName = ((topic == null) ? "weekly" : topic.getTopicName());

            final TopStoriesForTopicResponse topStoriesForTopicResponse = groupTopStoriesByDateResponse.new TopStoriesForTopicResponse(topicName);

            //Creates the model for a topic
            digestsPerTopic.get(topic).stream()
                    .filter(digest -> digest.getStoryId() != null)
                    .forEach(digest -> {
                        //Creates the model for a story
                        final String storyTitle = digest.getStoryId().getTitle();
                        final String storyUrl = digest.getStoryId().getUrl();
                        final Integer storyScore = digest.getStoryId().getScore();

                        final TopStoryResponse topStoryResponse = topStoriesForTopicResponse.new TopStoryResponse(storyTitle, storyUrl, storyScore);

                        topStoriesForTopicResponse.getTopStories().add(topStoryResponse);
                    });

            groupTopStoriesByDateResponse.getTopics().add(topStoriesForTopicResponse);
        });

        return groupTopStoriesByDateResponse;
    }
}
