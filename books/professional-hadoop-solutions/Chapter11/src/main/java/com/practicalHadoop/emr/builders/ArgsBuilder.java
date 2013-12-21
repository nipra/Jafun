package com.practicalHadoop.emr.builders;

import java.util.ArrayList;
import java.util.List;

public class ArgsBuilder extends BaseListBuilder<String> {

	private String prefix;
	
	protected ArgsBuilder(String prefix)
	{
		this.prefix = prefix;
	}

	@Override
	public List<String> build()
	{
		List<String> result = new ArrayList<String>();
		this.buildList(result);
		return result;
	}
	
	@Override
	protected String build(int index)
	{
		return this.getArgValue(String.format(prefix + "Args.M%d", index));
	}
	
	protected String getArgValue(String argName)
	{
		return this.getValue(argName);
	}
}
