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
public class GetAndUpdateNewStoriesJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "GetAndUpdateNewStoriesJobBean")
    @Autowired
    public Job newStoriesJobMethod(final @Qualifier("GetAndUpdateNewStoriesStepBean") Step getAndUpdateNewStoriesStep,
                                   final @Qualifier("TopStoryPerTopicStepBean") Step topStoriesPerTopicStep) {
        return jobBuilderFactory.get("GetAndUpdateNewStoriesJobName")
                .incrementer(new RunIdIncrementer())
                .flow(getAndUpdateNewStoriesStep)
                .next(topStoriesPerTopicStep)
                .end()
                .build();
    }
}
