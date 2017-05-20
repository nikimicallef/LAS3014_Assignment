package com.uom.las3014.schedule;

import com.uom.las3014.batching.jobs.NewStoriesJob;
import com.uom.las3014.batching.readers.NewStoriesReader;
import com.uom.las3014.batching.steps.NewStoriesStep;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class NewStoriesBatchJobScheduler {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private NewStoriesJob newStoriesJob;

    @Autowired
    private NewStoriesStep newStoriesStep;

    @Autowired
    private NewStoriesReader newStoriesReader;

    @Autowired
    private HackernewsRequester hackernewsRequester;

    //TODO: Configure this hourly
    @Scheduled(fixedDelay = 60_000, initialDelay = 1_000)
    public void perform() throws Exception {
        newStoriesReader.setNewStoryIds(hackernewsRequester.getNewStories().iterator());

        final JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        jobLauncher.run(newStoriesJob.newStoriesJobMethod(newStoriesStep.newStoriesStepMethod()), param);
    }
}
