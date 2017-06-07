package com.uom.las3014.batching.processors;

import com.google.common.collect.Ordering;
import com.uom.las3014.dao.*;
import com.uom.las3014.service.StoriesService;
import com.uom.las3014.service.UserTopicMappingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Specific {@link ItemProcessor} which creates a weekly {@link Digest} for all {@link Topic}, using a maximum of 3 {@link Story}
 * If no top {@link Story} is found a {@link Digest} with {@link Digest#storyId} is created
 */
@Component
@StepScope
public class WeeklyTopStoriesDigestPerTopicProcessor implements ItemProcessor<Topic,List<Digest>> {
    @Autowired
    private StoriesService storiesService;

    @Autowired
    private UserTopicMappingService userTopicMappingService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Value("#{jobParameters['dateTimeExecutedMillis']}")
    public long dateTimeExecutedMillis;

    @Override
    public List<Digest> process(final Topic topic) throws Exception {
        final Timestamp createdAfter = new Timestamp(dateTimeExecutedMillis - TimeUnit.DAYS.toMillis(7));
        final List<Story> stories = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(topic.getTopicName(), createdAfter);

        final Timestamp interestedToIsAfter = new Timestamp(dateTimeExecutedMillis);
        final Timestamp interestedFromBefore = new Timestamp(dateTimeExecutedMillis);
        final List<UserTopicMapping> userTopicMapping = userTopicMappingService
                .findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfterAndInterestedFromBefore(topic, interestedToIsAfter, interestedFromBefore);


        final Set<User> usersToMapWithDigest = userTopicMapping.stream().map(UserTopicMapping::getUser).collect(Collectors.toSet());

        final List<Story> topStories = Ordering.from(Story::compareTo).greatestOf(stories, 3);

        final Date dayOfWeek = new Date(dateTimeExecutedMillis);

        if(topStories.size() > 0){
            topStories.forEach(story -> logger.debug(topic.getTopicName() +" has top story " + story.getStoryId() + " " + story.getTitle() + " " + story.getScore()));
            return topStories.stream().map(story -> {
                return new Digest(dayOfWeek, topic, story, usersToMapWithDigest);
            }).collect(Collectors.toList());
        } else {
            final Digest newDigest = new Digest(dayOfWeek, topic, null, usersToMapWithDigest);
            return new ArrayList<>(Collections.singleton(newDigest));
        }
    }
}
