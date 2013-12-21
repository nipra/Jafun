package com.practicalHadoop.emr.builders;

import com.practicalHadoop.emr.builders.utils.JobFlowContext;

public abstract class BaseBuilder<TEntity> {

	protected static final String DEBUG_JAR_NAME_PROPERTY = "DebugConf.Jar";
	protected static final String HBASE_JAR_NAME_PROPERTY = "HBaseConf.Jar";
	protected static final String HBASE_MAIN_CLASS = "emr.hbase.backup.Main";
	
	public abstract TEntity build();
	
	//Gets value from a template. If not found - throws an exception
	protected String getValueOrError(String key, String exMessage)
	{
		String value = this.getValue(key);
		if (value == null) 
		{
			throw new IllegalStateException(exMessage);
		}
		return value;
	}
	
	//Gets value from a template. If not found - loads from a default configuration
	protected String getValueOrLoadDefault(String key)
	{
		String value = this.getValue(key);
		if (value != null)
		{
			return value;
		}
		
		return loadDefaultValue(key);
	}
	
	//Gets value from a template. If not found - returns null
	protected String getValue(String key)
	{
		return getValueOrDefault(key, null);
	}
	
	//Gets value from a template. If not found - returns specified default value
	protected String getValueOrDefault(String key, String defaultValue)
	{
		String value = JobFlowContext.getTemplate().getProperty(key, defaultValue);
		return (value == null ? null : this.cleanValue(value));
	}
	
	protected String loadDefaultValue(String key)
	{
		String value = JobFlowContext.getDefaultTemplate().getProperty(key);
		return (value == null ? null : this.cleanValue(value));
	}
	
	//Replaces placeholders with values, makes trim
	private String cleanValue(String value)
	{
		return value.replace("{jobInstanceName}", JobFlowContext.getInstanceName()).trim();
	}
		
}
