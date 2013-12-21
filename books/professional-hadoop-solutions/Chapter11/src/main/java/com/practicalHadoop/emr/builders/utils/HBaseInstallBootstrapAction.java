package com.practicalHadoop.emr.builders.utils;

import com.amazonaws.services.elasticmapreduce.model.BootstrapActionConfig;
import com.amazonaws.services.elasticmapreduce.model.ScriptBootstrapActionConfig;
import com.practicalHadoop.emr.builders.BaseBuilder;

public class HBaseInstallBootstrapAction extends BaseBuilder<BootstrapActionConfig> {
	
	@Override
	public BootstrapActionConfig build()
	{
		ScriptBootstrapActionConfig scriptBootstrapAction = new ScriptBootstrapActionConfig();
		scriptBootstrapAction.setPath("s3://elasticmapreduce/bootstrap-actions/setup-hbase");
		
		BootstrapActionConfig result = new BootstrapActionConfig("Install HBase", scriptBootstrapAction);
		System.out.println(result.toString());
		
		return result;
	}

}
