package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class TopStoryPerTopicProcessor implements ItemProcessor<Topic, Topic> {
    @Autowired
    private StoriesService storiesService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public Topic process(Topic topic) throws Exception {
        final LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth(),
                9,
                0,
                0,
                0);

        final List<Story> stories = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(topic.getTopicName(), new Timestamp(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - TimeUnit.DAYS.toMillis(7)));

        final Optional<Story> topStoryOpt = stories.stream().max(Comparator.comparing(Story::getScore));

        final Story topStory = topStoryOpt.orElse(null);

        if(topStory != null) {
            logger.debug(topic.getTopicName() + " has top story " + topStory.getScore() + " and ID " + topStory.getStoryId());
            topic.setTopStoryId(topStory);
        }

        return topic;
    }
}
