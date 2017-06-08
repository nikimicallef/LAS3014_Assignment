package com.uom.las3014.schedule.batch;

import com.uom.las3014.batching.jobs.GetAndUpdateTopStoriesJob;
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

/**
 * A {@link Scheduled} task which runs the {@link GetAndUpdateTopStoriesJob}
 */
@Configuration
public class GetAndUpdateTopStoriesBatchJobScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("GetAndUpdateTopStoriesJobBean")
    private Job newStoriesJob;

    @Scheduled(cron = "0 0 * * * *")
    @CacheEvict(value ={MyCacheManager.TOPIC_CACHE, MyCacheManager.TOP_STORY_CACHE}, allEntries = true)
    public void performNewStoriesJob() throws Exception {
        final JobParameters param = new JobParametersBuilder()
                                        .addString("JobID", String.valueOf(System.currentTimeMillis()))
                                        .toJobParameters();

        jobLauncher.run(newStoriesJob, param);
    }
}
