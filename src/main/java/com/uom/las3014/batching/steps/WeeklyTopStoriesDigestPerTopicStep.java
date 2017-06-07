package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.WeeklyTopStoriesDigestPerTopicProcessor;
import com.uom.las3014.batching.readers.generic.GetAllTopicsReader;
import com.uom.las3014.batching.writers.WeeklyTopStoriesDigestPerTopicWriter;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Specific {@link Step} which creates a weekly {@link Digest} for the top {@link Story} irrelevant of {@link Topic}
 */
@Component
public class WeeklyTopStoriesDigestPerTopicStep {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private GetAllTopicsReader getAllTopicsReader;

    @Autowired
    private WeeklyTopStoriesDigestPerTopicProcessor weeklyTopStoriesDigestPerTopicProcessor;

    @Autowired
    private WeeklyTopStoriesDigestPerTopicWriter weeklyTopStoriesDigestPerTopicWriter;

    @Bean(name = "WeeklyTopStoriesDigestPerTopicStepBean")
    public Step weeklyTopStoriesPerTopicStepMethod() {
        return stepBuilderFactory.get("WeeklyTopStoriesDigestPerTopicStep")
                                    .<Topic, List<Digest>>chunk(100)
                                    .reader(getAllTopicsReader)
                                    .processor(weeklyTopStoriesDigestPerTopicProcessor)
                                    .writer(weeklyTopStoriesDigestPerTopicWriter)
                                    .build();
    }
}
