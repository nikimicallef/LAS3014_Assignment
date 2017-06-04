package com.uom.las3014.schedule.batch;

import com.uom.las3014.cache.MyCacheManager;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
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
    @CacheEvict(value = MyCacheManager.TOPIC_CACHE, allEntries = true)
//    @Scheduled(fixedDelay = 999_000, initialDelay = 300_000)
    public void performUpdateWeeklyStoriesJob() throws Exception {
        final JobParameters param = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(updateStoriesJob, param);
    }
}
