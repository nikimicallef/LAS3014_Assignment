package com.uom.las3014.schedule.batch;

import com.uom.las3014.batching.jobs.CreateDigestsJob;
import com.uom.las3014.cache.MyCacheManager;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.service.DigestsService;
import com.uom.las3014.service.StoriesService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * A {@link Scheduled} task which does the below
 * a) Runs the {@link CreateDigestsJob}
 * b) Deletes all {@link Digest} which are older that one year. This will also cascade rows in the UserDigestMapping table.
 * c) Deletes all {@link Story} which are older than a week and have no associated digests.
 */
@Configuration
public class CreateDigestsScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("CreateDigestsJobBean")
    private Job createDigestsJob;

    @Autowired
    private DigestsService digestsService;

    @Autowired
    private StoriesService storiesService;

    @CacheEvict(value = MyCacheManager.DIGESTS_CACHE, allEntries = true)
    @Scheduled(cron = "0 0 9 ? * SAT")
    public void performDigestsJob() throws Exception {
        final LocalDateTime dateTimeExecuted = LocalDateTime.of(LocalDate.now().getYear(),
                                                                LocalDate.now().getMonth(),
                                                                LocalDate.now().getDayOfMonth(),
                                                                9,
                                                                0,
                                                                0,
                                                                0);

        final long dateTimeExecutedMillis = dateTimeExecuted.atZone(ZoneId.systemDefault())
                                                            .toInstant()
                                                            .toEpochMilli();
        final String currentTime = String.valueOf(System.currentTimeMillis());

        final JobParameters param = new JobParametersBuilder()
                                        .addString("JobID", currentTime)
                                        .addLong("dateTimeExecutedMillis", dateTimeExecutedMillis)
                                        .toJobParameters();

        jobLauncher.run(createDigestsJob, param);

        final Timestamp yearBeforeDateTimeExecuted = new Timestamp(dateTimeExecuted.minusYears(1)
                                                                                   .atZone(ZoneId.systemDefault())
                                                                                   .toInstant()
                                                                                   .toEpochMilli());
        digestsService.deleteDigestByDayOfWeekBefore(yearBeforeDateTimeExecuted);

        final Timestamp weekBeforeDateTimeExecuted = new Timestamp(dateTimeExecuted.minusWeeks(1)
                                                                                   .atZone(ZoneId.systemDefault())
                                                                                   .toInstant()
                                                                                   .toEpochMilli());
        storiesService.deleteByDateCreatedBeforeAndDigestsEmpty(weekBeforeDateTimeExecuted);
    }
}
