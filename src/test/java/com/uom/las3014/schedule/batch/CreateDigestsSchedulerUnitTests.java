package com.uom.las3014.schedule.batch;

import com.uom.las3014.batching.jobs.CreateDigestsJob;
import com.uom.las3014.batching.steps.WeeklyTopStoriesDigestPerTopicStep;
import com.uom.las3014.batching.steps.WeeklyTopStoriesDigestStep;
import com.uom.las3014.service.DigestsService;
import com.uom.las3014.service.StoriesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CreateDigestsSchedulerUnitTests {
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private CreateDigestsJob createDigestsJob;
    @Mock
    private WeeklyTopStoriesDigestPerTopicStep weeklyTopStoriesDigestPerTopicStep;
    @Mock
    private WeeklyTopStoriesDigestStep weeklyTopStoriesDigestStep;
    @Mock
    private DigestsService digestsService;
    @Mock
    private StoriesService storiesService;
    @InjectMocks
    private CreateDigestsScheduler createDigestsScheduler;

    @Before
    public void setUp() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(createDigestsScheduler,
                                    "createDigestsJob",
                                    createDigestsJob.newStoriesJobMethod(weeklyTopStoriesDigestPerTopicStep.weeklyTopStoriesPerTopicStepMethod(),
                                                                         weeklyTopStoriesDigestStep.weeklyTopStoriesPerTopicStepMethod()));

        when(jobLauncher.run(eq(createDigestsJob.newStoriesJobMethod(weeklyTopStoriesDigestPerTopicStep.weeklyTopStoriesPerTopicStepMethod(),
                                                                  weeklyTopStoriesDigestStep.weeklyTopStoriesPerTopicStepMethod())),
                             any(JobParameters.class)))
                        .thenReturn(new JobExecution(1L));
        doNothing().when(digestsService).deleteDigestByDayOfWeekBefore(any(Timestamp.class));
        doNothing().when(storiesService).deleteByDateCreatedBeforeAndDigestsEmpty(any(Timestamp.class));
    }

    @Test
    public void performWeeklyTopStoriesPerTopicJob_jobRunsOk() throws Exception {
        createDigestsScheduler.performDigestsJob();

        verify(jobLauncher)
                .run(eq(createDigestsJob.newStoriesJobMethod(weeklyTopStoriesDigestPerTopicStep.weeklyTopStoriesPerTopicStepMethod(),
                                                          weeklyTopStoriesDigestStep.weeklyTopStoriesPerTopicStepMethod())),
                     any(JobParameters.class));
        verify(digestsService).deleteDigestByDayOfWeekBefore(any(Timestamp.class));
        verify(storiesService).deleteByDateCreatedBeforeAndDigestsEmpty(any(Timestamp.class));
    }
}
