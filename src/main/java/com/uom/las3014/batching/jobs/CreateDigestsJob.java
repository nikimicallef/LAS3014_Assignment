package com.uom.las3014.batching.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateDigestsJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "CreateDigestsJobBean")
    @Autowired
    public Job newStoriesJobMethod(final @Qualifier("WeeklyTopStoriesDigestPerTopicStepBean") Step weeklyTopStoriesDigestPerTopic,
                                   final @Qualifier("WeeklyTopStoriesDigestBean") Step weeklyTopStoriesDigestBean) {
        return jobBuilderFactory.get("CreateDigestsJobName")
        .incrementer(new RunIdIncrementer())
        .flow(weeklyTopStoriesDigestPerTopic)
        .next(weeklyTopStoriesDigestBean)
        .end()
        .build();
        }
}