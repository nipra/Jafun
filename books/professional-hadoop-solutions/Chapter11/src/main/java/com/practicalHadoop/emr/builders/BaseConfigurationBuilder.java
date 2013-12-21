package com.practicalHadoop.emr.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.elasticmapreduce.model.BootstrapActionConfig;
import com.amazonaws.services.elasticmapreduce.model.ScriptBootstrapActionConfig;

public class BaseConfigurationBuilder extends BaseBuilder<BootstrapActionConfig> {

	private String argsSeparator;
	private String propertiesPrefix;
	private String bootstrapActionName;
	private String bootstrapActionPath;

	public BaseConfigurationBuilder(String propertiesPrefix, String bootstrapActionName, String bootstrapActionPath)
	{
		this.init(null, propertiesPrefix, bootstrapActionName, bootstrapActionPath);
	}
	
	public BaseConfigurationBuilder(String argsSeparator, String propertiesPrefix, String bootstrapActionName, String bootstrapActionPath)
	{
		this.init(argsSeparator, propertiesPrefix, bootstrapActionName, bootstrapActionPath);
	}
	
	private void init(String argsSeparator, String propertiesPrefix, String bootstrapActionName, String bootstrapActionPath)
	{
		this.argsSeparator = argsSeparator;
		this.propertiesPrefix = propertiesPrefix;
		this.bootstrapActionName = bootstrapActionName;
		this.bootstrapActionPath = bootstrapActionPath;
	}
	
	@Override
	public BootstrapActionConfig build()
	{	
		//Default Args
		DefaultArgsBuilder defaultArgsBuilder = new DefaultArgsBuilder(propertiesPrefix);
		List<String> defaultArgs = defaultArgsBuilder.build();
		
		//Args
		ArgsBuilder argsBuilder = new ArgsBuilder(propertiesPrefix);
		List<String> args = argsBuilder.build();
		
		//Merge
		HashMap<String, String> hashMap = new HashMap<String, String>();
		buildArgMap(defaultArgs, hashMap);
		
		buildArgMap(args, hashMap);
		
		List<String> mergedArgs = new ArrayList<String>(hashMap.size());
		for(String key : hashMap.keySet())
		{
			if (this.argsSeparator != null)
			{
				mergedArgs.add(argsSeparator);
			}
			
			mergedArgs.add(key + "=" + hashMap.get(key));
		}
		
		ScriptBootstrapActionConfig scriptBootstrapAction = new ScriptBootstrapActionConfig(bootstrapActionPath, mergedArgs);
		BootstrapActionConfig result = new BootstrapActionConfig(bootstrapActionName, scriptBootstrapAction);
		
		System.out.println(result.toString());
		return result;
	}

	private void buildArgMap(List<String> args, HashMap<String, String> hashMap)
	{
		for(String arg : args)
		{
			int index = arg.indexOf("=");
			String key = arg.substring(0, index);
			String value = arg.substring(index + 1, arg.length());
			hashMap.put(key, value);
		}
	}
}
