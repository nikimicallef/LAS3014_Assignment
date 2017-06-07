package com.uom.las3014.batching.jobs;

import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Batch job which does the following processes
 * a) Gets all {@link Story} created within the last 7 days and the HackerNews API is queried with the story ID,
 *    where the {@link Story} details are updated (namely score and/or isDeleted flag).
 * b) Find the top {@link Story} per {@link Topic} posted within the last 24 hours.
 */
@Configuration
public class UpdateStoriesJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "UpdateStoriesJobBean")
    @Autowired
    public Job newStoriesJobMethod(final @Qualifier("UpdateStoriesStepBean") Step updateStoriesStep,
                                   final @Qualifier("TopStoryPerTopicStepBean") Step topStoriesPerTopicStep) {
        return jobBuilderFactory.get("UpdateStoriesJobName")
                .incrementer(new RunIdIncrementer())
                .flow(updateStoriesStep)
                .next(topStoriesPerTopicStep)
                .end()
                .build();
    }
}
