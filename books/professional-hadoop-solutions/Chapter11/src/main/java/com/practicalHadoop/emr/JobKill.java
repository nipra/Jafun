package com.practicalHadoop.emr;

import java.io.File;
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.SetTerminationProtectionRequest;
import com.amazonaws.services.elasticmapreduce.model.TerminateJobFlowsRequest;

public final class JobKill
{
	private static AmazonElasticMapReduce emr;

	private JobKill()
	{
	}

	private static void init(File propFile) throws IOException
	{
		AWSCredentials credentials = new PropertiesCredentials(propFile);
		emr = new AmazonElasticMapReduceClient(credentials);
	}

	public static void main(String[] args) throws IOException
	{

		if (args.length < 2)
		{
			System.err.println("Usage: JobKill <AWS_credentials> <job_flow_id>\n");
			System.exit(-1);
		}
		
		String awsCredentials = args[0];
		String jobFlowID = args[1];
		killJob(jobFlowID, awsCredentials);
		System.err.println("Shutting down job flow " + jobFlowID + "...");
		new JobStatus(emr).checkStatus(jobFlowID);
	}
	
	public static void killJob(String jobFlowID, String awsCredentials)
	{
		try {
			init(new File(awsCredentials));
		} catch (Exception e) {
			throw new RuntimeException("Failed to connect to AWS", e); 
		}
		
		killJob(jobFlowID, emr);
	}
	
	public static void killJob(String jobFlowID, AmazonElasticMapReduce emr)
	{
		TerminateJobFlowsRequest request = new TerminateJobFlowsRequest().withJobFlowIds(jobFlowID);
		SetTerminationProtectionRequest terminationProtectionRequest = new SetTerminationProtectionRequest().withJobFlowIds(jobFlowID);
		terminationProtectionRequest.setTerminationProtected(false);
		emr.setTerminationProtection(terminationProtectionRequest);
		emr.terminateJobFlows(request);
	}
}
