package com.uom.las3014.schedule;

import com.uom.las3014.batching.readers.NewStoriesReader;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class NewStoriesBatchJobScheduler {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("NewStoriesJobBean")
    private Job newStoriesJob;

    @Autowired
    private NewStoriesReader newStoriesReader;

    @Autowired
    private HackernewsRequester hackernewsRequester;

    //TODO: Configure this hourly
    //TODO: What happens to job if DB goes down or HN goesdown?
    @Scheduled(cron = "0 0 2 1/1 * ?")
//    @Scheduled(fixedDelay = 60_000, initialDelay = 1_000)
    public void perform() throws Exception {
        final List<String> newStories = hackernewsRequester.getNewStories().orElse(new ArrayList<>());

        newStoriesReader.setNewStoryIds(newStories.iterator());

        final JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        jobLauncher.run(newStoriesJob, param);
    }
}
