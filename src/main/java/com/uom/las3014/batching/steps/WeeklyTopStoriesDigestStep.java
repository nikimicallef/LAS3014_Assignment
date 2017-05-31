package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.WeeklyTopStoriesDigestProcessor;
import com.uom.las3014.batching.readers.WeeklyTopStoriesDigestReader;
import com.uom.las3014.batching.writers.WeeklyTopStoriesDigestWriter;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WeeklyTopStoriesDigestStep {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private WeeklyTopStoriesDigestReader weeklyTopStoriesDigestReader;

    @Autowired
    private WeeklyTopStoriesDigestProcessor weeklyTopStoriesDigestProcessor;

    @Autowired
    private WeeklyTopStoriesDigestWriter weeklyTopStoriesDigestWriter;

    @Bean(name = "WeeklyTopStoriesDigestStepBean")
    public Step weeklyTopStoriesPerTopicStepMethod() {
        return stepBuilderFactory.get("WeeklyTopStoriesDigestStep")
                .<Story, Digest>chunk(3)
                .reader(weeklyTopStoriesDigestReader)
                .processor(weeklyTopStoriesDigestProcessor)
                .writer(weeklyTopStoriesDigestWriter)
                .build();
    }
}