package com.practicalHadoop.emr.builders;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.elasticmapreduce.model.BootstrapActionConfig;
import com.amazonaws.services.elasticmapreduce.model.ScriptBootstrapActionConfig;
import com.practicalHadoop.emr.builders.utils.HBaseConfigurationBuilder;
import com.practicalHadoop.emr.builders.utils.HBaseDaemonsConfigurationBuilder;
import com.practicalHadoop.emr.builders.utils.HBaseInstallBootstrapAction;
import com.practicalHadoop.emr.builders.utils.HadoopConfigurationBuilder;

public class BootstrapActionBuilder extends BaseListBuilder<BootstrapActionConfig> {
	
	@Override
	public List<BootstrapActionConfig> build()
	{
		List<BootstrapActionConfig> result = new ArrayList<BootstrapActionConfig>();
		
		//Configure Hadoop
		result.add(new HadoopConfigurationBuilder().build());
		
		Boolean ifStartHBase = this.getValueOrDefault("HBase.Start", "false").equals("true");
		if (ifStartHBase)
		{
			//Install HBase
			result.add(new HBaseInstallBootstrapAction().build());
			
			//Configure HBase Daemons
			result.add(new HBaseDaemonsConfigurationBuilder().build());
			
			//Configure HBase
			result.add(new HBaseConfigurationBuilder().build());
		}
		
		this.buildList(result);
		return result;
	}
	
	@Override
	protected BootstrapActionConfig build(int index)
	{
		String bootstrapActionPrefix = String.format("BootstrapActions.M%d.", index);		
		
		//Name
		String name = this.getValue(bootstrapActionPrefix + "Name");
		if (name == null)
		{
			return null;
		}
		
		//Path
		String path = this.getValueOrError(bootstrapActionPrefix + "Path", "Missing property \"" + bootstrapActionPrefix + "Path\". This property describes the path to the script. Example: s3://mapreduceBucket/bootstrap/action.sh"); 
		
		//Args
		ArgsBuilder argsBuilder = new ArgsBuilder(bootstrapActionPrefix);
		List<String> args = argsBuilder.build();

		ScriptBootstrapActionConfig scriptBootstrapAction = new ScriptBootstrapActionConfig(path, args);
		BootstrapActionConfig result = new BootstrapActionConfig(name, scriptBootstrapAction);
				
		System.out.println(result.toString());
		return result;
	}
}
