package com.uom.las3014.batching.readers;

import com.uom.las3014.httpconnection.HackernewsRequester;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@StepScope
public class NewStoriesReader implements ItemReader<String> {
    private Iterator<String> newStoryIds;

    @Autowired
    public NewStoriesReader(final HackernewsRequester hackernewsRequester) {
        final List<String> newStories = hackernewsRequester.getNewStories().orElse(new ArrayList<>());

        newStoryIds = newStories.iterator();
    }

    @Override
    public String read() throws Exception {
        return newStoryIds.hasNext() ? newStoryIds.next() : null;
    }
}
