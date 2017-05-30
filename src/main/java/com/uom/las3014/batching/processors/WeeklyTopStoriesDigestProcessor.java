package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class WeeklyTopStoriesDigestProcessor implements ItemProcessor<Story, Digest>{

    @Override
    public Digest process(Story story) throws Exception {
        final LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth(),
                9,
                0,
                0,
                0);

        return new Digest(Timestamp.valueOf(localDateTime), null, story);
    }
}
