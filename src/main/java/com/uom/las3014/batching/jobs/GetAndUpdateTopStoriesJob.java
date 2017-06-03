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
public class GetAndUpdateTopStoriesJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "GetAndUpdateTopStoriesJobBean")
    @Autowired
    public Job newStoriesJobMethod(final @Qualifier("GetAndUpdateTopStoriesStepBean") Step getAndUpdateTopStoriesStep,
                                   final @Qualifier("TopStoryPerTopicStepBean") Step topStoriesPerTopicStep) {
        return jobBuilderFactory.get("GetAndUpdateTopStoriesJobName")
                .incrementer(new RunIdIncrementer())
                .flow(getAndUpdateTopStoriesStep)
                .next(topStoriesPerTopicStep)
                .end()
                .build();
    }
}
