package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@StepScope
public class WeeklyTopStoriesDigestProcessor implements ItemProcessor<Story, Digest>{

    @Value("#{jobParameters['dateTimeExecutedMillis']}")
    public long dateTimeExecutedMillis;

    @Override
    public Digest process(Story story) throws Exception {
        return new Digest(new Timestamp(dateTimeExecutedMillis), null, story);
    }
}
