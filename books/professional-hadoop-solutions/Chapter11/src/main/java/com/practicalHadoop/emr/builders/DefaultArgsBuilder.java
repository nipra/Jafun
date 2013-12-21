package com.practicalHadoop.emr.builders;


public class DefaultArgsBuilder extends ArgsBuilder {

	protected DefaultArgsBuilder(String prefix)
	{
		super(prefix);
	}

	@Override
	protected String getArgValue(String argName)
	{
		return this.loadDefaultValue(argName);
	}
}
