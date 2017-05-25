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
public class UpdateStoriesJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "UpdateStoriesJobBean")
    @Autowired
    public Job newStoriesJobMethod(final @Qualifier("UpdateStoriesStepBean") Step updateStoriesStep,
                                   final @Qualifier("TopStoryPerTopicStepBean") Step topStoriesPerTopic) {
        return jobBuilderFactory.get("UpdateStoriesJobName")
                .incrementer(new RunIdIncrementer())
                .flow(updateStoriesStep)
                .next(topStoriesPerTopic)
                .end()
                .build();
    }
}
