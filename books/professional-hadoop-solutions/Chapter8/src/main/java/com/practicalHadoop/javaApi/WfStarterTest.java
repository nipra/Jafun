package com.practicalHadoop.javaApi;

import static org.junit.Assert.*;

import java.util.Properties;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.WorkflowJob.Status;
import org.junit.BeforeClass;
//import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WfStarterTest
{
    private static final Logger logger = LoggerFactory.getLogger("WfStarterTest");
    
    static final private String OOZ_URL = "http://arch024:11000/oozie/";
    static final private String JOB_TRACKER = "arch023.hq.navteq.com:8021";
    static final private String NAME_NODE = "hdfs://arch021.hq.navteq.com:8020";
    static final private String WF_LOCATION_URL = "${nameNode}/user/ayakubov/dataPrep/workflow.xml";
    static final private String INPUT_DATA = "/user/ayakubov/data/";
    static final private String HIVE_DEFAULT_XML_PATH = "${nameNode}/sharedlib/conf-xml/hive-default.xml";
    static final private String WF_APP_LIB = ">${nameNode}/user/ayakubov/dataPrep/lib/chapter7-0.0.1-SNAPSHOT.jar";
    
    static WfStarter starter;
    static Properties confProp;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        confProp = new Properties();
        confProp.setProperty(OozieClient.APP_PATH, WF_LOCATION_URL);
        confProp.setProperty("oozie.wf.application.lib", WF_APP_LIB);
        confProp.setProperty("jobTracker", JOB_TRACKER);
        confProp.setProperty("nameNode", NAME_NODE);
        confProp.setProperty("user.name", "ayakubov");
        confProp.setProperty("user.password", "Navteq07");
        confProp.setProperty("input.data", INPUT_DATA);
        confProp.setProperty("HIVE_DEFAULT_XML_PATH", HIVE_DEFAULT_XML_PATH);
        starter =  WfStarter.createWfStarter(confProp, OOZ_URL);
    }

    //@Test
    public void testStarter() throws Exception
    {
        String jobID = starter.startJob();
        logger.info(" ** started the job: " + jobID);
        
        Thread.sleep(30 * 1000);
        
        Status status = starter.getJobStatus(jobID);
        logger.info(" ** job status: " + status);
        
        while(status == Status.RUNNING)     // can also add the job time threshold
        {
            logger.info(" ** job status: " + status);
            status = starter.getJobStatus(jobID);
        }
        
        logger.info(" ** job finished with the status: " + status);
        
        assertEquals(status, Status.SUCCEEDED);
    }
}
