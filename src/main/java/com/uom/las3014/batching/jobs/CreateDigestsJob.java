package com.uom.las3014.batching.jobs;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
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
 * a) Creates a new {@link Digest} which contains the top 3 {@link Story} per {@link Topic} for each {@link User}'s preference.
 * b) Creates a new {@link Digest} which contains the top 3 {@link Story} irrelevant of the {@link User}'s {@link Topic} preference.
 *
 * In both cases only stories posted within the last 7 days (by the second) of the batch job being run are considered.
 * In both cases the {@link Digest} table is populated along with the UserDigestMapping table.
 */
@Configuration
public class CreateDigestsJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "CreateDigestsJobBean")
    @Autowired
    public Job newStoriesJobMethod(final @Qualifier("WeeklyTopStoriesDigestPerTopicStepBean") Step weeklyTopStoriesDigestPerTopicStepBean,
                                   final @Qualifier("WeeklyTopStoriesDigestStepBean") Step weeklyTopStoriesDigestStepBean) {
        return jobBuilderFactory.get("CreateDigestsJobName")
                                .incrementer(new RunIdIncrementer())
                                .flow(weeklyTopStoriesDigestPerTopicStepBean)
                                .next(weeklyTopStoriesDigestStepBean)
                                .end()
                                .build();
    }
}