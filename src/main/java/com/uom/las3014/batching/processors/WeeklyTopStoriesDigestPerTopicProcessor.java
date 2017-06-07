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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public List<Digest> process(Topic topic) throws Exception {
        final List<Story> stories = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(topic.getTopicName(),
                                                        new Timestamp(dateTimeExecutedMillis - TimeUnit.DAYS.toMillis(7)));

        final List<UserTopicMapping> userTopicMapping = userTopicMappingService.findAllByTopicIsAndInterestedToIsNullOrInterestedToIsAfterAndInterestedFromBefore(topic, new Timestamp(dateTimeExecutedMillis), new Timestamp(dateTimeExecutedMillis));

        final Set<User> usersToMapWithDigest = userTopicMapping.stream().map(UserTopicMapping::getUser).collect(Collectors.toSet());

        final List<Story> topStories = Ordering.from(Story::compareTo).greatestOf(stories, 3);

        if(topStories.size() > 0){
            topStories.forEach(story -> logger.debug(topic.getTopicName() +" has top story " + story.getStoryId() + " " + story.getTitle() + " " + story.getScore()));
            return topStories.stream().map(story -> new Digest(new Date(dateTimeExecutedMillis), topic, story, usersToMapWithDigest)).collect(Collectors.toList());
        } else {
            final Digest newDigest = new Digest(new Date(dateTimeExecutedMillis), topic, null, usersToMapWithDigest);
            return new ArrayList<>(Collections.singleton(newDigest));
        }
    }
}
