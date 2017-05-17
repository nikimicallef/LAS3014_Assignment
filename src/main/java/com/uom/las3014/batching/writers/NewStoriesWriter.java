package com.uom.las3014.batching.writers;

import com.uom.las3014.dao.Story;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by niki on 17/05/17.
 */
@Component
public class NewStoriesWriter implements ItemWriter<Story> {
    private final Log logger = LogFactory.getLog(this.getClass());


    @Override
    public void write(List<? extends Story> list) throws Exception {
        list.forEach(story -> logger.info(story.toString()));
    }
}
