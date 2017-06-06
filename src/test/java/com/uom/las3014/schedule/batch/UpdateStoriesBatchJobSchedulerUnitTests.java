package com.uom.las3014.schedule.batch;

import com.uom.las3014.batching.jobs.GetAndUpdateTopStoriesJob;
import com.uom.las3014.batching.jobs.UpdateStoriesJob;
import com.uom.las3014.batching.steps.GetNewStoriesStep;
import com.uom.las3014.batching.steps.TopStoryPerTopicStep;
import com.uom.las3014.batching.steps.UpdateStoriesStep;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateStoriesBatchJobSchedulerUnitTests {
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private UpdateStoriesJob updateStoriesJob;
//    @Mock
//    private GetNewStoriesStep getNewStoriesStep;
    @Mock
    private UpdateStoriesStep updateStoriesStep;
    @Mock
    private TopStoryPerTopicStep topStoryPerTopicStep;
    @InjectMocks
    private UpdateStoriesBatchJobScheduler updateStoriesBatchJobScheduler;

    @Before
    public void setUp() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(updateStoriesBatchJobScheduler,
                                     "updateStoriesJob",
                                     updateStoriesJob.newStoriesJobMethod(updateStoriesStep.updateStoriesStepMethod(),
                                                                          topStoryPerTopicStep.updateStoriesStepMethod()));


        when(jobLauncher.run(eq(updateStoriesJob.newStoriesJobMethod(updateStoriesStep.updateStoriesStepMethod(),
                                                                     topStoryPerTopicStep.updateStoriesStepMethod())),
                             any(JobParameters.class)))
                        .thenReturn(new JobExecution(1L));
    }

    @Test
    public void performNewStoriesJob_jobRunsOk() throws Exception {
        updateStoriesBatchJobScheduler.performUpdateWeeklyStoriesJob();

        verify(jobLauncher).run(eq(updateStoriesJob.newStoriesJobMethod(updateStoriesStep.updateStoriesStepMethod(),
                                                                                                       topStoryPerTopicStep.updateStoriesStepMethod())),
                                                                  any(JobParameters.class));
    }
}
