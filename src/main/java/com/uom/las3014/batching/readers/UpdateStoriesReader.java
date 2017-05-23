package com.uom.las3014.batching.readers;

import com.uom.las3014.dao.Story;
import com.uom.las3014.service.StoriesService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@StepScope
public class UpdateStoriesReader implements ItemReader<Story> {
    private Iterator<Story> storiesToUpdate;

    @Autowired
    public UpdateStoriesReader(@Value("#{jobParameters['identifier']}") String identifier, StoriesService storiesService) {
        if(identifier.equals("week")){
            storiesToUpdate = storiesService.getLastWeeksUndeletedTopics().iterator();
        } else if (identifier.equals("12hrs")){
            storiesToUpdate = storiesService.get12HrsUndeletedTopics().iterator();
        } else {
            throw new IllegalArgumentException();
        }

        this.storiesToUpdate = storiesToUpdate;
    }

    @Override
    public Story read() throws Exception {
        return storiesToUpdate.hasNext() ? storiesToUpdate.next() : null;
    }
}
