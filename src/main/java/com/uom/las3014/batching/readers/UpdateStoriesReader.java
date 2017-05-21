package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Story;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class UpdateStoriesReader implements ItemReader<Story> {
    private Iterator<Story> storiesToUpdate;

    public void setStoriesToUpdate(Iterator<Story> storiesToUpdate) {
        this.storiesToUpdate = storiesToUpdate;
    }

    @Override
    public Story read() throws Exception {
        return storiesToUpdate.hasNext() ? storiesToUpdate.next() : null;
    }
}
