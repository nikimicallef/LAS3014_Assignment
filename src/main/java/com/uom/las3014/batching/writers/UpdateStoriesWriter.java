package com.uom.las3014.batching.writers;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateStoriesWriter implements ItemWriter<Story> {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private StoriesService storiesService;

    @Override
    public void write(List<? extends Story> list) throws Exception {
        list.forEach(story -> {
            storiesService.createNewOrUpdateExistingStory(story);
        });
    }
}
