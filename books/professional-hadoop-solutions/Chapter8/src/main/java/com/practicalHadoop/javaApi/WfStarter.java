package com.practicalHadoop.javaApi;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.WorkflowJob.Status;

public class WfStarter
{
    final OozieClient oozClient;
    final Properties confProp;

    private static final Logger logger = LoggerFactory.getLogger("WfStarter");
    
    public static WfStarter createWfStarter(Properties confProp, String oozServerURL)
    {
        return new WfStarter(confProp, oozServerURL);
    }
    
    private WfStarter(Properties confProp, String oozServerURL)
    {
        this.confProp = confProp;
        this.oozClient = new OozieClient(oozServerURL);
        logger.info(" actual client class - " + oozClient.getClass().getName());
    }

    public String startJob() throws OozieClientException
    {
        logger.info(" ** submitting workflow ");
        String oozJobID = oozClient.run(confProp);
        return oozJobID;
    }

    
    // returns one of enum value: PREP, RUNNING, SUCCEEDED, KILLED, FAILED, SUSPENDED 
    // (see org.apache.oozie.client.WorkflowJob.Status)
    public Status getJobStatus(String jobID) throws OozieClientException
    {
        logger.info(" ** submitting job status request ");
        WorkflowJob job = oozClient.getJobInfo(jobID);
        return job.getStatus();
    }
}

