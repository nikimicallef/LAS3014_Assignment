package com.uom.las3014.schedule;

import com.uom.las3014.batching.readers.NewStoriesReader;
import com.uom.las3014.batching.readers.UpdateStoriesReader;
import com.uom.las3014.httpconnection.HackernewsRequester;
import com.uom.las3014.service.StoriesService;
import com.uom.las3014.service.UserService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class UpdateStoriesBatchJobScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("UpdateStoriesJobBean")
    private Job updateStoriesJob;

    @Autowired
    private UpdateStoriesReader updateStoriesReader;

    @Autowired
    private StoriesService storiesService;

    //TODO: Set to cron
//    @Scheduled(cron = "0 0 2 1/1 * ?")
    @Scheduled(fixedDelay = 60_000, initialDelay = 1_000)
    public void perform() throws Exception {
        updateStoriesReader.setStoriesToUpdate(storiesService.getLastWeeksUndeletedTopics().iterator());

        final JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        jobLauncher.run(updateStoriesJob, param);
    }


    //TODO: Update later stories more frequently than older stories
//    @Scheduled(fixedDelay = 70_000, initialDelay = 1_000)
//    public void perform1() throws Exception {
//        updateStoriesReader.setStoriesToUpdate(storiesService.getLastWeeksUndeletedTopics().iterator());
//
//        final JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
//
//        jobLauncher.run(updateStoriesJob, param);
//    }
}
