package com.practicalHadoop.emr.builders.utils;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.elasticmapreduce.model.ActionOnFailure;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.practicalHadoop.emr.builders.BaseBuilder;

public class HBaseRestoreStep extends BaseBuilder<StepConfig> {
	
	private String restorePath;
	
	public HBaseRestoreStep(String restorePath)
	{
		this.restorePath = restorePath;
	}
	
	@Override
	public StepConfig build()
	{		
		HadoopJarStepConfig hadoopJarStep = new HadoopJarStepConfig();
		hadoopJarStep.setJar(this.getValueOrLoadDefault(HBASE_JAR_NAME_PROPERTY));
		hadoopJarStep.setMainClass(HBASE_MAIN_CLASS);
		List<String> args = new ArrayList<String>(3);
		args.add("--restore");
		args.add("--backup-dir");
		args.add(this.restorePath);
		hadoopJarStep.setArgs(args);
		
		StepConfig result = new StepConfig("Restore HBase", hadoopJarStep);
		result.setActionOnFailure(ActionOnFailure.TERMINATE_JOB_FLOW);
		
		System.out.println(result.toString());
		return result;
	}
}
