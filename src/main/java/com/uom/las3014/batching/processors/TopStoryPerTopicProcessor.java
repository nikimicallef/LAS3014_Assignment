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
        final List<Story> topStoryContainingKeyword = storiesService.getUndeletedStoriesContainingKeywordAndAfterTimestamp(topic.getTopicName(), new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24)));

        final Optional<Story> topStoryOpt = topStoryContainingKeyword.stream().max(Comparator.comparing(Story::getScore));

        final Story topStory = topStoryOpt.orElse(null);

        if(topStory != null) {
            logger.debug(topic.getTopicName() + " has top story " + topStory.getScore() + " and ID " + topStory.getStoryId());
            topic.setTopStoryId(topStory);
        } else {
            logger.debug(topic.getTopicName() + " has no top story");
            topic.setTopStoryId(null);
        }

        return topic;
    }
}
