package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.UpdateStoriesProcessor;
import com.uom.las3014.batching.readers.UpdateStoriesReader;
import com.uom.las3014.batching.writers.UpdateStoriesWriter;
import com.uom.las3014.dao.Story;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UpdateStoriesStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private UpdateStoriesReader updateStoriesReader;

    @Autowired
    private UpdateStoriesProcessor updateStoriesProcessor;

    @Autowired
    private UpdateStoriesWriter updateStoriesWriter;

    @Bean(name = "UpdateStoriesStepBean")
    public Step updateStoriesStepMethod() {
        return stepBuilderFactory.get("UpdateStoriesStep")
                .<Story, Story>chunk(100)
                .reader(updateStoriesReader)
                .processor(updateStoriesProcessor)
                .writer(updateStoriesWriter)
                .build();
    }
}
