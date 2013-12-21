package com.practicalHadoop.emr;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowResult;
import com.practicalHadoop.emr.builders.JobFlowBuilder;

public final class JobInvoker
{
	private JobInvoker()
	{
	}
	
	/**
	 * Submit MapReduce job to the cluster and wait for it to finish.
	 * 
	 * @param awsCredentialsPropFile AWS credential file
	 * @param jobInstanceName Name of the job instance
	 * @param jobFlowConfig EMR job flow
	 * @return True if job was successfully completed. Otherwise, return false.
	 * @throws IOException
	 */
	public static boolean waitForCompletion(File awsCredentialsPropFile, String jobInstanceName, Properties jobFlowConfig)
			throws IOException
	{
		return waitForCompletion(new PropertiesCredentials(awsCredentialsPropFile), jobInstanceName, jobFlowConfig);
	}

	/**
	 * Submit MapReduce job to the cluster and wait for it to finish.
	 * 
	 * @param AWSCredentials AWS Credentials
	 * @param jobInstanceName Name of the job instance
	 * @param jobFlowConfig EMR job flow
	 * @return True if job was successfully completed. Otherwise, return false.
	 * @throws IOException
	 */
	public static boolean waitForCompletion(AWSCredentials awsCredential, String jobInstanceName, Properties jobFlowConfig)
			throws IOException
	{
		String jobFlowID = submitJob(awsCredential, jobInstanceName, jobFlowConfig);
		
		System.out.println("Job Flow Id: " + jobFlowID);

		int ret = new JobStatus(awsCredential).checkStatus(jobFlowID);
		return (ret == 0);
	}
	
	/**
	 * Submit MapReduce job to the cluster and return immediately.
	 * 
	 * @param awsCredential
	 * @param jobInstanceName Name of the job instance
	 * @param jobFlowConfig
	 * @return String job flow Id
	 * @throws IOException
	 */
	public static String submitJob(AWSCredentials awsCredential, String jobInstanceName, Properties jobFlowConfig)
			throws IOException
	{
		// build job flow request
		RunJobFlowRequest request = (new JobFlowBuilder(jobInstanceName, jobFlowConfig)).build();
		
		// Start job flow
		return startJobFlow(request, new AmazonElasticMapReduceClient(awsCredential));
	}

	private static String startJobFlow(RunJobFlowRequest request, AmazonElasticMapReduce emr)
	{
		// Start job flow
		String jobFlowID = null;
		try
		{
			// Run the job flow
			RunJobFlowResult result = emr.runJobFlow(request);
			jobFlowID = result.getJobFlowId();
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
		
		return jobFlowID;
	}
}
