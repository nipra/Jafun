package com.practicalHadoop.emr.builders.utils;

import com.practicalHadoop.emr.builders.BaseConfigurationBuilder;

public class HBaseConfigurationBuilder extends BaseConfigurationBuilder {
	
	private static final String ARGS_SEPARATOR = "-s";
	private static final String PREFIX = "HBaseConf.";
	private static final String NAME = "Configure HBase";
	private static final String PATH = "s3://us-east-1.elasticmapreduce/bootstrap-actions/configure-hbase";
	
	public HBaseConfigurationBuilder()
	{
		super(ARGS_SEPARATOR, PREFIX, NAME, PATH);
	}
}
