package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Specific {@link ItemReader} which gets aòò {@link Story} where {@link Story#deleted} is false and {@link Story#dateCreated}
 *     is 7 days behind. Defined as {@link StepScope} so it is created at each step execution
 */
@Component
@StepScope
public class UpdateStoriesReader implements ItemReader<Story> {
    private Iterator<Story> storiesToUpdate;

    @Autowired
    public UpdateStoriesReader(final StoriesService storiesService) {
        final Timestamp createdAfter = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7));
        this.storiesToUpdate = storiesService.getUndeletedTopicsAfterTimestamp(createdAfter).iterator();
    }

    @Override
    public Story read() throws Exception {
        return storiesToUpdate.hasNext() ? storiesToUpdate.next() : null;
    }
}
