package com.uom.las3014.schedule;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

//TODO: Configure error handling and multi threaded
@Configuration
public class UpdateStoriesBatchJobScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("UpdateStoriesJobBean")
    private Job updateStoriesJob;

    //TODO: Set to cron
    @Scheduled(cron = "0 0 2 ? * *")
//    @Scheduled(fixedDelay = 999_000, initialDelay = 1_000)
    public void performUpdateWeeklyStoriesJob() throws Exception {
        final JobParameters param = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .addString("identifier", "week")
                .toJobParameters();

        jobLauncher.run(updateStoriesJob, param);
    }

//    @Scheduled(fixedDelay = 9999_000, initialDelay = 120_000)
    @Scheduled(cron = "0 0 * * * ?")
    public void performUpdate12hStoriesJob() throws Exception {
        final JobParameters param = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .addString("identifier", "12hrs")
                .toJobParameters();

        jobLauncher.run(updateStoriesJob, param);
    }
}
