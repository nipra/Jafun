package com.practicalHadoop.emr.builders;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.practicalHadoop.emr.builders.utils.DebugStartStep;
import com.practicalHadoop.emr.builders.utils.HBaseBackupStep;
import com.practicalHadoop.emr.builders.utils.HBaseRestoreStep;
import com.practicalHadoop.emr.builders.utils.HBaseStartStep;

public class StepBuilder extends BaseListBuilder<StepConfig> {
	@Override
	public List<StepConfig> build()
	{
		List<StepConfig> result = new ArrayList<StepConfig>();
		
		Boolean isDebug = this.getValueOrDefault("Debug.Start", "false").equals("true");
		if (isDebug)
		{
			result.add(new DebugStartStep().build());
		}
		
		Boolean ifStartHBase = this.getValueOrDefault("HBase.Start", "false").equals("true");
		if (ifStartHBase)
		{
			result.add(new HBaseStartStep().build());
		}
		
		this.buildList(result);
		
		return result;
	}

	@Override
	protected StepConfig build(int index)
	{
		return this.buildSpecificStep(index);
	}
	
	public StepConfig buildSpecificStep(int index)
	{
		String stepPrefix = String.format("Steps.M%d.", index);		
		
		String name = this.getValue(stepPrefix + "Name");
		if (name != null) {
			return this.buildHadoopStep(name, stepPrefix);
		}
		
		String restorePath = this.getValue(stepPrefix + "RestoreHBasePath");
		if (restorePath != null)
		{
			return new HBaseRestoreStep(restorePath).build();
		}
		
		String backupPath = this.getValue(stepPrefix + "BackupHBasePath");
		if (backupPath != null)
		{
			return new HBaseBackupStep(backupPath).build();
		}
		
		return null;
	}
	
	private StepConfig buildHadoopStep(String name, String stepPrefix)
	{
		//Jar
		String jar = this.getValueOrError(stepPrefix + "Jar", "Missing property \"" + stepPrefix + "Jar\". This property describes the path to the jar. Example: s3://mapreduceBucket/jar/myApplication.jar");
		HadoopJarStepConfig hadoopJarStep = new HadoopJarStepConfig(jar);
		
		//ActionOnFailure
		String actionOnFailure = this.getValue(stepPrefix + "ActionOnFailure");
		
		//MainClass
		String mainClass = this.getValue(stepPrefix + "MainClass");
		if (mainClass != null)
		{
			hadoopJarStep.setMainClass(mainClass);
		}
		
		//Args
		ArgsBuilder argsBuilder = new ArgsBuilder(stepPrefix);
		List<String> args = argsBuilder.build();
		hadoopJarStep.setArgs(args);
		
		StepConfig result = new StepConfig(name, hadoopJarStep);
		if (actionOnFailure != null) 
		{
			result.setActionOnFailure(actionOnFailure);
		}
		
		System.out.println(result.toString());
		return result;
	}
}
