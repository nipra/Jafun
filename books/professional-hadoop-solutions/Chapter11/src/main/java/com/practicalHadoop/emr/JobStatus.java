package com.practicalHadoop.emr;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.DescribeJobFlowsRequest;
import com.amazonaws.services.elasticmapreduce.model.DescribeJobFlowsResult;
import com.amazonaws.services.elasticmapreduce.model.JobFlowDetail;
import com.amazonaws.services.elasticmapreduce.model.JobFlowExecutionState;
import com.amazonaws.services.elasticmapreduce.model.StepDetail;
import com.amazonaws.services.elasticmapreduce.model.StepExecutionStatusDetail;

public final class JobStatus
{
	private static final int SECONDS_30 = 30000;
	private static final int JOB_FAILED_RETCODE = -1;
	private static final int JOB_COMPLETED_RETCODE = 0;
	
	private static final List<JobFlowExecutionState> DONE_STATES = Arrays
			.asList(new JobFlowExecutionState[] { JobFlowExecutionState.COMPLETED, JobFlowExecutionState.FAILED,
					JobFlowExecutionState.WAITING, JobFlowExecutionState.TERMINATED });
	
	private AmazonElasticMapReduce emr;
	
	public JobStatus(File propFile) throws IOException
	{
		AWSCredentials credentials = new PropertiesCredentials(propFile);
		emr = new AmazonElasticMapReduceClient(credentials);
	}
	
	public JobStatus(AWSCredentials credentials) throws IOException
	{
		emr = new AmazonElasticMapReduceClient(credentials);
	}
	
	public JobStatus(AmazonElasticMapReduce emr) throws IOException
	{
		this.emr = emr;
	}
	
	public static void main(String[] args) throws IOException
	{
		if (args.length < 2)
		{
			System.err.println("Usage: JobStatus <AWS_credentials> <job_flow_id>\n");
			System.exit(JOB_FAILED_RETCODE);
		}
		
		String awsCredentials = args[0];
		String jobFlowID = args[1];
		JobStatus status = null;
		
		status = new JobStatus(new File(awsCredentials));
		status.checkStatus(jobFlowID);
	}
	
	public int checkStatus(String jobFlowID)
	{
		int result = 0;
		String lastState = "";
		String lastStep = "";
		
		STATUS_LOOP: while (true)
		{
			DescribeJobFlowsRequest desc = new DescribeJobFlowsRequest(Arrays.asList(new String[] { jobFlowID }));
			
			DescribeJobFlowsResult descResult = emr.describeJobFlows(desc);
			
			for (JobFlowDetail detail : descResult.getJobFlows())
			{
				String state = detail.getExecutionStatusDetail().getState();
				if (isDone(state))
				{
					System.out.println("Job " + state + ": " + detail.toString());
					if (state.equals(JobFlowExecutionState.COMPLETED.name()))
					{
						result = JOB_COMPLETED_RETCODE;
					}
					else if (state.equals(JobFlowExecutionState.WAITING.name()))
					{
						result = getStateForWaitingJob(detail);
					}
					else
					{
						result = JOB_FAILED_RETCODE;
					}
					break STATUS_LOOP;
				}
				else
				{
					if (!lastState.equals(state))
					{
						lastState = state;
						System.out.println("Job " + detail.getName() + " " + state + " at " + new Date().toString());
					}
					
					lastStep = getLastRunningStepName(detail, lastStep);
				}
			}
			
			try
			{
				Thread.sleep(SECONDS_30);
			}
			catch (InterruptedException e)
			{
				// ignore
			}
		}
		
		return result;
	}
	
	private String getLastRunningStepName(JobFlowDetail detail, String lastStep)
	{
		List<StepDetail> steps = detail.getSteps();
		
		for (StepDetail step : steps)
		{
			StepExecutionStatusDetail sd = step.getExecutionStatusDetail();
			if (sd.getState().equals("RUNNING") && !lastStep.equals(step.getStepConfig().getName()))
			{
				System.out.println("Step "
						+ step.getStepConfig().getName()
						+ ", status "
						+ sd.getState()
						+ ", started at "
						+ sd.getStartDateTime());
				return step.getStepConfig().getName();
			}
		}
		
		return lastStep;
	}
	
	private int getStateForWaitingJob(JobFlowDetail detail)
	{
		List<StepDetail> steps = detail.getSteps();
		for (StepDetail step : steps)
		{
			StepExecutionStatusDetail sd = step.getExecutionStatusDetail();
			if (!sd.getState().equals("COMPLETED"))
			{
				return JOB_FAILED_RETCODE;
			}
		}
		
		return JOB_COMPLETED_RETCODE;
	}
	
	private static boolean isDone(String value)
	{
		JobFlowExecutionState state = JobFlowExecutionState.fromValue(value);
		return DONE_STATES.contains(state);
	}
}
