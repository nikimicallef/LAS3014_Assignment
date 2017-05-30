package com.uom.las3014.schedule.batch;

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

//TODO: Configure error handling and multi threaded
@Configuration
public class GetAndUpdateNewStoriesBatchJobScheduler {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("GetAndUpdateNewStoriesJobBean")
    private Job newStoriesJob;

    //TODO: Configure this hourly
    //TODO: What happens to job if DB goes down or HN goesdown?
//    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedDelay = 999_000, initialDelay = 1_000)
    public void performNewStoriesJob() throws Exception {
        final JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        jobLauncher.run(newStoriesJob, param);
    }
}
