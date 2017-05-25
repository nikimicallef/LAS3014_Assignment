package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.TopStoryPerTopicProcessor;
import com.uom.las3014.batching.readers.TopStoryPerTopicReader;
import com.uom.las3014.batching.writers.TopStoryPerTopicWriter;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class TopStoryPerTopicStep {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TopStoryPerTopicReader topStoryPerTopicReader;

    @Autowired
    private TopStoryPerTopicProcessor topStoryPerTopicProcessor;

    @Autowired
    private TopStoryPerTopicWriter topStoryPerTopicWriter;

    @Bean(name = "TopStoryPerTopicStepBean")
    public Step updateStoriesStepMethod() {
        return stepBuilderFactory.get("TopStoryPerTopicStep")
                .<Topic, Pair<Topic,Story>>chunk(100)
                .reader(topStoryPerTopicReader)
                .processor(topStoryPerTopicProcessor)
                .writer(topStoryPerTopicWriter)
                .build();
    }
}
