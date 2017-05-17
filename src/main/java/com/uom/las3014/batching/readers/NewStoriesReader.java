package com.uom.las3014.batching.readers;

import com.uom.las3014.httpconnection.HackernewsRequester;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by niki on 17/05/17.
 */
@Component
public class NewStoriesReader implements ItemReader<String> {

    private final HackernewsRequester hackernewsRequester;

    private final Iterator<String> newStoryIds;

    @Autowired
    public NewStoriesReader(HackernewsRequester hackernewsRequester) throws IOException {
        this.hackernewsRequester = hackernewsRequester;
        newStoryIds = hackernewsRequester.getNewStories().iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return newStoryIds.hasNext() ? newStoryIds.next() : null;
    }
}
