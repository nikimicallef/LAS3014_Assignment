package com.uom.las3014.batching.readers;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Created by niki on 17/05/17.
 */
@Component
public class NewStoriesReader implements ItemReader<String> {
    private Iterator<String> newStoryIds;

    public void setNewStoryIds(final Iterator<String> newStoryIds){
        this.newStoryIds = newStoryIds;
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return newStoryIds.hasNext() ? newStoryIds.next() : null;
    }
}
