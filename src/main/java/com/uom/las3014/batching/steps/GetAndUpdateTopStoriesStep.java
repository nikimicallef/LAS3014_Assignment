package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.GetAndUpdateTopStoriesProcessor;
import com.uom.las3014.batching.readers.GetAndUpdateTopStoriesReader;
import com.uom.las3014.batching.writers.GetAndUpdateTopStoriesWriter;
import com.uom.las3014.dao.Story;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GetAndUpdateTopStoriesStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private GetAndUpdateTopStoriesReader getAndUpdateTopStoriesReader;

    @Autowired
    private GetAndUpdateTopStoriesProcessor getAndUpdateTopStoriesProcessor;

    @Autowired
    private GetAndUpdateTopStoriesWriter getAndUpdateTopStoriesWriter;

    @Bean(name = "GetAndUpdateTopStoriesStepBean")
    public Step newStoriesStepMethod() {
        return stepBuilderFactory.get("GetAndUpdateTopStoriesStep")
                .<String, Story>chunk(100)
                .reader(getAndUpdateTopStoriesReader)
                .processor(getAndUpdateTopStoriesProcessor)
                .writer(getAndUpdateTopStoriesWriter)
                .faultTolerant()
                .retry(IOException.class)
                .retryLimit(3)
                .build();
    }
}
