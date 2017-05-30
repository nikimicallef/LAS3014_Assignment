package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.GetAndUpdateNewStoriesProcessor;
import com.uom.las3014.batching.readers.GetAndUpdateNewStoriesReader;
import com.uom.las3014.batching.writers.GetAndUpdateNewStoriesWriter;
import com.uom.las3014.dao.Story;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GetAndUpdateNewStoriesStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private GetAndUpdateNewStoriesReader getAndUpdateNewStoriesReader;

    @Autowired
    private GetAndUpdateNewStoriesProcessor getAndUpdateNewStoriesProcessor;

    @Autowired
    private GetAndUpdateNewStoriesWriter getAndUpdateNewStoriesWriter;

    @Bean(name = "GetAndUpdateNewStoriesStepBean")
    public Step newStoriesStepMethod() {
        return stepBuilderFactory.get("GetAndUpdateNewStoriesStep")
                .<String, Story>chunk(100)
                .reader(getAndUpdateNewStoriesReader)
                .processor(getAndUpdateNewStoriesProcessor)
                .writer(getAndUpdateNewStoriesWriter)
                .build();
    }
}
