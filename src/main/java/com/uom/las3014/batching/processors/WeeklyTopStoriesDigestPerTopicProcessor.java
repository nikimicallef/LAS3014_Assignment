package com.uom.las3014.batching.processors;

import com.google.common.collect.Ordering;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.service.StoriesService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class WeeklyTopStoriesDigestPerTopicProcessor implements ItemProcessor<Topic,List<Digest>> {
    @Autowired
    private StoriesService storiesService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public List<Digest> process(Topic topic) throws Exception {
        final LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth(),
                9,
                0,
                0,
                0);

        final List<Story> stories = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(topic.getTopicName(), new Timestamp(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - TimeUnit.DAYS.toMillis(7)));

        final List<Story> topStories = Ordering.from(Story::compareTo).greatestOf(stories, 3);

        topStories.forEach(story -> logger.debug(topic.getTopicName() +" has top story " + story.getStoryId() + " " + story.getTitle() + " " + story.getScore()));

        return topStories.stream().map(story -> new Digest(Timestamp.valueOf(localDateTime), topic, story)).collect(Collectors.toList());
    }
}
