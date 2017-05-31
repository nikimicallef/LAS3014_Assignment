package com.uom.las3014.batching.processors;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.service.UserService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashSet;

@Component
@StepScope
public class WeeklyTopStoriesDigestProcessor implements ItemProcessor<Story, Digest>{

    @Autowired
    private UserService userService;

    @Value("#{jobParameters['dateTimeExecutedMillis']}")
    public long dateTimeExecutedMillis;

    @Override
    public Digest process(Story story) throws Exception {
        return new Digest(new Date(dateTimeExecutedMillis), null, story, new HashSet<>(userService.getAllUsers()));
    }
}
