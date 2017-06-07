package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Specific {@link ItemReader} which gets the top 3 {@link Story} by {@link Story#score} where {@link Story#deleted} is false and {@link Story#dateCreated}
 *     is 7 days behind. Used to create a weekly {@link Digest} for the top {@link Story} in the last 7 days.
 *     Defined as {@link StepScope} so it is created at each step execution
 */
@Component
@StepScope
public class WeeklyTopStoriesDigestReader implements ItemReader<Story> {
    private Iterator<Story> topStories;

    @Value("#{jobParameters['dateTimeExecutedMillis']}")
    public long dateTimeExecutedMillis;

    @Autowired
    public WeeklyTopStoriesDigestReader(final StoriesService storiesService) {
        final Timestamp dateAfter = new Timestamp(dateTimeExecutedMillis - TimeUnit.DAYS.toMillis(7));
        topStories = storiesService.getTop3UndeletedStoriesAfterTimestamp(dateAfter).iterator();
    }

    @Override
    public Story read() throws Exception {
        return topStories.hasNext() ? topStories.next() : null;
    }
}
