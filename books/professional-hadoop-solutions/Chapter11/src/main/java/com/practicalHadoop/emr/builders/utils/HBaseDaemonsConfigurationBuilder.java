package com.practicalHadoop.emr.builders.utils;

import com.practicalHadoop.emr.builders.BaseConfigurationBuilder;

public class HBaseDaemonsConfigurationBuilder extends BaseConfigurationBuilder {
	
	private static final String PREFIX = "HBaseDaemonsConf.";
	private static final String NAME = "Configure HBase Daemons";
	private static final String PATH = "s3://us-east-1.elasticmapreduce/bootstrap-actions/configure-hbase-daemons";
	
	public HBaseDaemonsConfigurationBuilder()
	{
		super(PREFIX, NAME, PATH);
	}
}
