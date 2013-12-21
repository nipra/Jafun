package com.practicalHadoop.emr.builders.utils;

import com.practicalHadoop.emr.builders.BaseConfigurationBuilder;

public class HadoopConfigurationBuilder  extends BaseConfigurationBuilder
{
	private static final String ARGS_SEPARATOR = "-s";
	private static final String PREFIX = "HadoopConf.";
	private static final String NAME = "Configure Hadoop";
	private static final String PATH = "s3://elasticmapreduce/bootstrap-actions/configure-hadoop";
	
	public HadoopConfigurationBuilder()
	{
		super(ARGS_SEPARATOR, PREFIX, NAME, PATH);
	}
}
