package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.NewStoriesProcessor;
import com.uom.las3014.batching.readers.NewStoriesReader;
import com.uom.las3014.batching.writers.NewStoriesWriter;
import com.uom.las3014.dao.Story;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class NewStoriesStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private NewStoriesReader newStoriesReader;

    @Autowired
    private NewStoriesProcessor newStoriesProcessor;

    @Autowired
    private NewStoriesWriter newStoriesWriter;

    @Bean
    public Step newStoriesStepMethod() {
        return stepBuilderFactory.get("NewStoriesStep")
                .<String, Story>chunk(100)
                .reader(newStoriesReader)
                .processor(newStoriesProcessor)
                .writer(newStoriesWriter)
                .build();
    }
}
