package com.practicalHadoop.emr.builders.utils;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.elasticmapreduce.model.ActionOnFailure;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.practicalHadoop.emr.builders.BaseBuilder;

public class DebugStartStep extends BaseBuilder<StepConfig> {
	
	@Override
	public StepConfig build()
	{
		HadoopJarStepConfig hadoopJarStep = new HadoopJarStepConfig();
		hadoopJarStep.setJar(this.getValueOrLoadDefault(DEBUG_JAR_NAME_PROPERTY));
		List<String> args = new ArrayList<String>(1);
		args.add("s3://elasticmapreduce/libs/state-pusher/0.1/fetch");
		hadoopJarStep.setArgs(args);
		
		StepConfig result = new StepConfig("Start debugging", hadoopJarStep);
		result.setActionOnFailure(ActionOnFailure.CONTINUE);
		
		System.out.println(result.toString());
		return result;
	}
}
