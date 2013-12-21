package com.practicalHadoop.emr.builders.utils;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.elasticmapreduce.model.ActionOnFailure;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.practicalHadoop.emr.builders.BaseBuilder;

public class HBaseBackupStep extends BaseBuilder<StepConfig> {
	
	private String backupPath;
	
	public HBaseBackupStep(String backupPath)
	{
		this.backupPath = backupPath;
	}
	
	@Override
	public StepConfig build()
	{		
		HadoopJarStepConfig hadoopJarStep = new HadoopJarStepConfig();
		hadoopJarStep.setJar(this.getValueOrLoadDefault(HBASE_JAR_NAME_PROPERTY));
		hadoopJarStep.setMainClass(HBASE_MAIN_CLASS);
		List<String> args = new ArrayList<String>(3);
		args.add("--backup");
		args.add("--backup-dir");
		args.add(backupPath);
		hadoopJarStep.setArgs(args);
		
		StepConfig result = new StepConfig("Backup HBase", hadoopJarStep);
		result.setActionOnFailure(ActionOnFailure.TERMINATE_JOB_FLOW);
		
		System.out.println(result.toString());
		return result;
	}
}
