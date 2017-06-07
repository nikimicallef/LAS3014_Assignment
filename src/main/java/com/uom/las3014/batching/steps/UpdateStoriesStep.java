package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.UpdateStoriesProcessor;
import com.uom.las3014.batching.readers.UpdateStoriesReader;
import com.uom.las3014.batching.writers.generic.SaveAllStoriesWriter;
import com.uom.las3014.dao.Story;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Specific {@link Step} which calls the {@link HackernewsRequester} and updates the {@link Story} variables including the {@link Story#deleted} and/or {@link Story#score}
 * This {@link Step} is fault tolerant and it retries 3 times for an {@link IOException}, possibly executed from the {@link HackernewsRequester}
 */
@Component
public class UpdateStoriesStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private UpdateStoriesReader updateStoriesReader;

    @Autowired
    private UpdateStoriesProcessor updateStoriesProcessor;

    @Autowired
    private SaveAllStoriesWriter saveAllStoriesWriter;

    @Bean(name = "UpdateStoriesStepBean")
    public Step updateStoriesStepMethod() {
        return stepBuilderFactory.get("UpdateStoriesStep")
                                    .<Story, Story>chunk(100)
                                    .reader(updateStoriesReader)
                                    .processor(updateStoriesProcessor)
                                    .writer(saveAllStoriesWriter)
                                    .faultTolerant()
                                    .retry(IOException.class)
                                    .retryLimit(3)
                                    .build();
    }
}
