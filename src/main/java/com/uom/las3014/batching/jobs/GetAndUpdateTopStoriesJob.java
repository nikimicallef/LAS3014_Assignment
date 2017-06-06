package com.uom.las3014.batching.jobs;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Batch job which does the following processes
 * a) Calls the HackerNews API to get the IDs of the latest 500 {@link Story} and they are persisted.
 * b) Find the top {@link Story} per {@link Topic} posted within the last 24 hours.
 */
@Configuration
public class GetAndUpdateTopStoriesJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "GetAndUpdateTopStoriesJobBean")
    @Autowired
    public Job newStoriesJobMethod(final @Qualifier("GetNewStoriesStepBean") Step getAndUpdateTopStoriesStep,
                                   final @Qualifier("TopStoryPerTopicStepBean") Step topStoriesPerTopicStep) {
        return jobBuilderFactory.get("GetAndUpdateTopStoriesJobName")
                .incrementer(new RunIdIncrementer())
                .flow(getAndUpdateTopStoriesStep)
                .next(topStoriesPerTopicStep)
                .end()
                .build();
    }
}