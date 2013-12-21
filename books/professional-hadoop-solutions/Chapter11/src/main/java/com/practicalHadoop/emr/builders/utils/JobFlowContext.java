package com.practicalHadoop.emr.builders.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class JobFlowContext
{
	private static String instanceName = null;
	private static Properties template = null;
	private static Properties defaultTemplate = null;
	private static final String CONFIGURATION_FILENAME = "emrDefault.properties";
	
	// class has to be "created" via init()
	private JobFlowContext() 
	{
	}
	
	public static String getInstanceName()
	{
		return instanceName;
	}

	public static Properties getTemplate()
	{
		return template;
	}

	public static Properties getDefaultTemplate()
	{
		return defaultTemplate;
	}

	public static synchronized void init(String jobInstanceName, Properties jobFlowTemplate) throws IOException
	{
		instanceName = jobInstanceName;
		template = jobFlowTemplate;
	
		if (defaultTemplate == null)
		{
			InputStream inStream = JobFlowContext.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILENAME);
			
			defaultTemplate = new Properties();
			defaultTemplate.load(inStream);
			inStream.close();
		}
	}
}
