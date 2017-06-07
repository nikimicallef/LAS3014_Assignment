package com.uom.las3014.batching.writers;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.service.DigestsService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Specific {@link ItemWriter} which saves a number of {@link Digest} grouped by {@link Topic} in a {@link List}
 */
@Component
public class WeeklyTopStoriesDigestPerTopicWriter implements ItemWriter<List<Digest>> {
    @Autowired
    private DigestsService digestsService;

    @Override
    public void write(final List<? extends List<Digest>> list) throws Exception {
        list.forEach(digestsService::saveAll);
    }
}
