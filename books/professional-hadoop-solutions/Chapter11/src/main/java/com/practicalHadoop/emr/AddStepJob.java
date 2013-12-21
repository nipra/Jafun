package com.practicalHadoop.emr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.AddJobFlowStepsRequest;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.practicalHadoop.emr.builders.StepBuilder;
import com.practicalHadoop.emr.builders.utils.JobFlowContext;

public class AddStepJob {

	private AmazonElasticMapReduce emr;
	private StepBuilder builder;
	private static final int JOB_FAILED_RETCODE = -1;
	
	public AddStepJob(File propFile, Properties jobFlowConfig) throws IOException
	{
		AWSCredentials credentials = new PropertiesCredentials(propFile);
		emr = new AmazonElasticMapReduceClient(credentials);
		JobFlowContext.init("", jobFlowConfig);
		builder = new StepBuilder();
	}
	
	public AddStepJob(AWSCredentials credentials, Properties jobFlowConfig) throws IOException
	{
		emr = new AmazonElasticMapReduceClient(credentials);
		JobFlowContext.init("", jobFlowConfig);
		builder = new StepBuilder();
	}
	
	public AddStepJob(AmazonElasticMapReduce emr, Properties jobFlowConfig) throws IOException
	{
		this.emr = emr;
		JobFlowContext.init("", jobFlowConfig);
		builder = new StepBuilder();
	}
		
	public void AddStep(String jobFlowID, int step){
		StepConfig stepConfig = builder.buildSpecificStep(step);
		List<StepConfig> steps = new ArrayList<StepConfig>(1);
		steps.add(stepConfig);
		AddJobFlowStepsRequest request = new AddJobFlowStepsRequest().
			withJobFlowId(jobFlowID).withSteps(steps);
		try{
			emr.addJobFlowSteps(request);
			return;
		}
		catch (AmazonServiceException ase)
		{
			throw new RuntimeException("Caught Exception: "
					+ ase.getMessage()
					+ " Response Status Code: "
					+ ase.getStatusCode()
					+ " Error Code: "
					+ ase.getErrorCode()
					+ " Request ID: "
					+ ase.getRequestId(), ase);
		}
	}
}
