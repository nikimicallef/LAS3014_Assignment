package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Story;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UpdateRecentStoriesScoreReader implements ItemReader<Story> {
    private Story storyToUpdate;

    public UpdateRecentStoriesScoreReader(){}

    public UpdateRecentStoriesScoreReader(final Story storyToUpdate) {
        this.storyToUpdate = storyToUpdate;
    }

    @Override
    public Story read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return storyToUpdate;
    }
}
