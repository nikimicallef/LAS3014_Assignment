package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component
@StepScope
public class WeeklyTopStoriesDigestReader implements ItemReader<Story> {
    private Iterator<Story> topStories;

    @Autowired
    public WeeklyTopStoriesDigestReader(final StoriesService storiesService) {
        final LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth(),
                9,
                0,
                0,
                0);

        topStories = storiesService.getTop3UndeletedStoriesAfterTimestamp(new Timestamp(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - TimeUnit.DAYS.toMillis(7))).iterator();
    }

    @Override
    public Story read() throws Exception {
        return topStories.hasNext() ? topStories.next() : null;
    }
}
