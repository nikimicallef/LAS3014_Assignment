package com.uom.las3014.batching.readers;

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

@Component
@StepScope
public class WeeklyTopStoriesDigestReader implements ItemReader<Story> {
    private Iterator<Story> topStories;

    @Value("#{jobParameters['dateTimeExecutedMillis']}")
    public long dateTimeExecutedMillis;

    @Autowired
    public WeeklyTopStoriesDigestReader(final StoriesService storiesService) {
        topStories = storiesService.getTop3UndeletedStoriesAfterTimestamp(new Timestamp(dateTimeExecutedMillis - TimeUnit.DAYS.toMillis(7))).iterator();
    }

    @Override
    public Story read() throws Exception {
        return topStories.hasNext() ? topStories.next() : null;
    }
}
