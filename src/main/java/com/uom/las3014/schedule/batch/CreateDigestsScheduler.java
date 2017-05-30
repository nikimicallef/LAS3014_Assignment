package com.uom.las3014.schedule.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CreateDigestsScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("CreateDigestsJobBean")
    private Job createDigestsJob;


    //TODO: Set to run at 9 am
    @Scheduled(fixedDelay = 999_000, initialDelay = 1_000)
    public void performWeeklyTopStoriesPerTopicJob() throws Exception {
        final JobParameters param = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(createDigestsJob, param);
    }
}
