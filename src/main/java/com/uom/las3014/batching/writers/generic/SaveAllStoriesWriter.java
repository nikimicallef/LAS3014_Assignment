package com.uom.las3014.batching.writers.generic;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaveAllStoriesWriter implements ItemWriter<Story> {
    @Autowired
    private StoriesService storiesService;

    @Override
    public void write(List<? extends Story> list) throws Exception {
        storiesService.saveAllStories(list);
    }
}
