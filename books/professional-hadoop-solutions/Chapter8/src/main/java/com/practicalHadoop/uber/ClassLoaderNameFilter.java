package com.practicalHadoop.uber;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Now implements only default - hard-coded - prefix pattern (in create() method)
 * That default assumes thet create() argument (sHint) is null
 * 
 * Extension can e.g. use Configuration object, or property file, or ...
 */
public class ClassLoaderNameFilter
{
	private List<String> prefList;
	
	static public ClassLoaderNameFilter create(String sHint)
	{
		ClassLoaderNameFilter filter;
		
		if(sHint == null)
			filter = createDefault();
		else
			filter = createFromString(sHint);
		
		return filter;
	}

	private static ClassLoaderNameFilter createDefault()
    {
		ClassLoaderNameFilter filter = new ClassLoaderNameFilter();
		filter.add("aly.");
		
	    return filter;
    }

	private static ClassLoaderNameFilter createFromString(String sHint)
	{
		ClassLoaderNameFilter filter = new ClassLoaderNameFilter();
		String [] prefArr = sHint.split(",");
		for(String pref : prefArr)
		{
			filter.add(pref);
		}
		return filter;
	}
	
	private ClassLoaderNameFilter()
	{
		prefList = new ArrayList<String>();
	}
	
	private void add(String pref)
    {
		prefList.add(pref);
    }
	
	public final boolean internalLoad(String className)
	{
		boolean bInternalLoad = false;

		for(String pref : prefList)
		{
			if(className.startsWith(pref))
			{
				bInternalLoad = true;
				break;
			}
		}
		
		return bInternalLoad;
	}
	
	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		for(String s : prefList)
		{
			buf.append(s);
			buf.append(".* : ");
		}
		return buf.toString();
	}
}
