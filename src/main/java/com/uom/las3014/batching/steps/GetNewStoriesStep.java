package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.processors.GetNewStoriesProcessor;
import com.uom.las3014.batching.readers.GetNewStoriesReader;
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
 * Specific {@link Step} which defines the components to get the new {@link Story} from the {@link HackernewsRequester} and persist them.
 * This {@link Step} is fault tolerant and it retries 3 times for an {@link IOException}, possibly executed from the {@link HackernewsRequester}
 */
@Component
public class GetNewStoriesStep {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private GetNewStoriesReader getNewStoriesReader;

    @Autowired
    private GetNewStoriesProcessor getNewStoriesProcessor;

    @Autowired
    private SaveAllStoriesWriter saveAllStoriesWriter;

    @Bean(name = "GetNewStoriesStepBean")
    public Step newStoriesStepMethod() {
        return stepBuilderFactory.get("GetNewStoriesStep")
                                    .<String, Story>chunk(100)
                                    .reader(getNewStoriesReader)
                                    .processor(getNewStoriesProcessor)
                                    .writer(saveAllStoriesWriter)
                                    .faultTolerant()
                                    .retry(IOException.class)
                                    .retryLimit(3)
                                    .build();
    }
}
