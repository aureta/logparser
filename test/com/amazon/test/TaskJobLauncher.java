package com.amazon.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:/path/to/job-launcher-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TaskJobLauncher {
	/** The job launcher. */
	@Autowired
	private JobLauncher m_jobLauncher;
	/** The job. */
	@Autowired
	@Qualifier("readFlatFile")
	private Job m_job;
	/** The job parameters. */
	private JobParameters m_jobParameters;

	/**
	 * Test run job.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testRunJob() throws Exception {
		JobExecution jobExecution = null;
		try {
			m_jobParameters = new JobParametersBuilder().addLong("run.id",
					new Date().getTime()).toJobParameters();

			jobExecution = m_jobLauncher.run(m_job, m_jobParameters);
			while (jobExecution.isRunning()) {

			}
			assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		} finally {
			if (jobExecution != null) {
				jobExecution.stop();
			}
		}
	}

}
