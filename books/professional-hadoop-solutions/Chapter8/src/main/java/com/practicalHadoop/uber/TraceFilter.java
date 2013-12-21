package com.practicalHadoop.uber;

import java.util.ArrayList;
import java.util.List;

public class TraceFilter
{
	private List<String> prefList;

	public static TraceFilter create()
    {
		TraceFilter traceFilter = new TraceFilter();
	    return traceFilter;
    }

	private TraceFilter()
	{
		prefList = new ArrayList<String>();
	}
	
	private void add(String s)
	{
		prefList.add(s);
	}
	
	boolean isTracing(String fullQualifiedClassName)
	{
		boolean bTrace = false;
		for(String pref : prefList)
		{
			if(fullQualifiedClassName.startsWith(pref))
			{
				bTrace = true;
				break;
			}
		}
		return bTrace;
	}
}
