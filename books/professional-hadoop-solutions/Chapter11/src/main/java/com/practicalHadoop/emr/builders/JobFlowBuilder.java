package com.practicalHadoop.emr.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.amazonaws.services.elasticmapreduce.model.BootstrapActionConfig;
import com.amazonaws.services.elasticmapreduce.model.InstanceGroupConfig;
import com.amazonaws.services.elasticmapreduce.model.InstanceRoleType;
import com.amazonaws.services.elasticmapreduce.model.JobFlowInstancesConfig;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.practicalHadoop.emr.builders.utils.JobFlowContext;

public class JobFlowBuilder extends BaseBuilder<RunJobFlowRequest>
{	
	public JobFlowBuilder(String jobInstanceName, Properties jobFlowTemplate) throws IOException
	{
		JobFlowContext.init(jobInstanceName, jobFlowTemplate);
	}

	@Override
	public RunJobFlowRequest build()
	{		
		System.out.println("---------------- Job Flow ----------------");
		
		String name = this.getValueOrError("Name", "Missing property \"Name\". This property describes jobflow name. Example: MyJobFlowName")
				+ ": " + JobFlowContext.getInstanceName();		
		System.out.println(String.format("JobFlow Name: %s", name));
		
		String logUri = this.getValueOrError("LogUri", "Missing property \"LogUri\". This property describes where to write jobflow logs. Example: s3://mapreduceBucket/log");
		System.out.println(String.format("JobFlow LogUri: %s", logUri));
		
		// Instance groups
		InstanceGroupConfig master = new InstanceGroupConfig();
		master.setInstanceRole(InstanceRoleType.MASTER);
		master.setInstanceCount(1);
		String masterType = this.getValueOrLoadDefault("MasterType");
		master.setInstanceType(masterType);
		System.out.println(String.format("Master Group, type %s, count %d", masterType, 1));

		InstanceGroupConfig core = new InstanceGroupConfig();
		core.setInstanceRole(InstanceRoleType.CORE);
		String coreType = this.getValueOrLoadDefault("CoreType");
		core.setInstanceType(coreType);
		int coreCount = Integer.parseInt(this.getValueOrLoadDefault("CoreCount"));
		core.setInstanceCount(coreCount);
		System.out.println(String.format("Core Group, type %s, count %d", coreType, coreCount));

		List<InstanceGroupConfig> instanceGroups = new ArrayList<InstanceGroupConfig>();
		instanceGroups.add(master);
		instanceGroups.add(core);

		// Get keepJobFlowAlive value from input parms 
		String keepJobFlowAliveStr = this.getValueOrLoadDefault("KeepJobflowAliveWhenNoSteps");
		boolean keepJobFlowAlive = (keepJobFlowAliveStr.toLowerCase().equals("true"));
		 
		// Get TerminationProtected value from input parms 
		String terminationProtectedStr = this.getValueOrLoadDefault("TerminationProtected");
		boolean terminationProtected = (terminationProtectedStr.toLowerCase().equals("true"));

		// Instances
		JobFlowInstancesConfig instances = new JobFlowInstancesConfig();
		instances.setInstanceGroups(instanceGroups);
		instances.setEc2KeyName(this.getValueOrLoadDefault("SecurityKeyPair"));
		instances.setKeepJobFlowAliveWhenNoSteps(keepJobFlowAlive);
		instances.setTerminationProtected(terminationProtected);
		instances.setHadoopVersion(this.getValueOrLoadDefault("HadoopVersion"));
		
		RunJobFlowRequest result = new RunJobFlowRequest(name, instances);
		result.setLogUri(logUri);
		result.setAmiVersion("latest");
		result.setVisibleToAllUsers(true);
		result.setBootstrapActions(this.buildBootstrapActions());
		result.setSteps(this.buildSteps());
		
		return result;
	}
	
	private List<BootstrapActionConfig> buildBootstrapActions()
	{
		System.out.println("---------------- BootstrapActions ----------------");
		return new BootstrapActionBuilder().build();
	}
	
	private List<StepConfig> buildSteps()
	{
		System.out.println("---------------- Steps ----------------");
		return new StepBuilder().build();
	}
}
