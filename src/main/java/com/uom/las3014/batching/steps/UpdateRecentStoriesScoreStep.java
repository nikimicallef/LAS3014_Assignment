package com.uom.las3014.batching.steps;

import com.uom.las3014.batching.readers.UpdateRecentStoriesScoreReader;
import com.uom.las3014.dao.Story;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UpdateRecentStoriesScoreStep {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private UpdateRecentStoriesScoreReader updateRecentStoriesScoreReader;

    @Bean
    public Step updateRecentStoriesScoreStepMethod() {
        return stepBuilderFactory.get("UpdateRecentStoriesScoreStepStep")
                .<Story, Story>chunk(1)
                .reader(updateRecentStoriesScoreReader)
                .build();
    }
}
