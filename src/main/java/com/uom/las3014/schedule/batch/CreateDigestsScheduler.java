package com.uom.las3014.schedule.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
        final LocalDateTime dateTimeExecuted = LocalDateTime.of(LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth(),
                9,
                0,
                0,
                0);

        final JobParameters param = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .addLong("dateTimeExecutedMillis", dateTimeExecuted.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .toJobParameters();

        jobLauncher.run(createDigestsJob, param);
    }
}
