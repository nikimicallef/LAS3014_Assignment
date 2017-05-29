package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.WeeklyTopStoriesPerTopicProcessor;
import com.uom.las3014.batching.readers.GetAllTopicsReader;
import com.uom.las3014.batching.writers.WeeklyTopStoriesPerTopicWriter;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Topic;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeeklyTopStoriesPerTopicStep {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private GetAllTopicsReader getAllTopicsReader;

    @Autowired
    private WeeklyTopStoriesPerTopicProcessor weeklyTopStoriesPerTopicProcessor;

    @Autowired
    private WeeklyTopStoriesPerTopicWriter weeklyTopStoriesPerTopicWriter;

    @Bean(name = "WeeklyTopStoriesPerTopicStepBean")
    public Step weeklyTopStoriesPerTopicStepMethod() {
        return stepBuilderFactory.get("WeeklyTopStoriesPerTopicStep")
                .<Topic, List<Digest>>chunk(100)
                .reader(getAllTopicsReader)
                .processor(weeklyTopStoriesPerTopicProcessor)
                .writer(weeklyTopStoriesPerTopicWriter)
                .build();
    }
}
