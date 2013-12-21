package com.practicalHadoop.uber.util;

import java.util.HashMap;
import java.util.Map;

public class PropertyMngr
{
    static public String getParamByKey(String[] args, String key)
	{
		String value = null;
		
		if(args == null || args.length == 0 || key == null || key.length() == 0)
			return value;
		
		
		for(String s : args)
		{
			String[] tokens = s.split("=");
			if(tokens.length != 2)
				continue;
			
			if(tokens[0].startsWith("-"))
			{
				String s1 = tokens[0];
				String s2 = s1.substring(1, tokens[0].length()); 
//				String sub = tokens[0].substring(1, tokens[0].length());
				if(key.equals(s2))
				{
					value = tokens[1];
					break;
				}
			}
		}
		return value;
	}
	
	static public String getParamByKeyPos(String[] args, String key, int pos)
	{
		if(args == null || args.length + 1 < pos || key == null || key.length() == 0)
			return null;
		
		String[] tokens = args[pos].split("=");
		if(tokens.length != 2)
			return null;
		
		
		if(tokens[0].startsWith("-"))
		{
			String flag = tokens[0].substring(1, tokens[0].length());
			if(key.equals(flag))
				return tokens[1];
		}
		return null;
	}

	/**
	 * special case - when "somebody" (like Hadoop) uses -conf AbraCadabra 
	 */
	static public String getConfParam(String[] args)
	{
		String value = null;
		
		if(args == null || args.length == 0)
			return value;
		
		for(int pos=0; pos<args.length; pos++ )
		{
			if(args[pos].startsWith("-conf"))
			{
				value = args[pos].substring(6);
				break;
			}
		}
		return value;
	}
	
    public static String showArgs(String [] args)
    {
        StringBuilder buf = new StringBuilder();
        
        int len = args.length;
        if(len == 0)
        {
            buf.append("\tzero arguments");
        }
        else
        {
            for(int argPos = 0; argPos < len; argPos++)
            {
                buf.append("\tpos ");
                buf.append(argPos);
                buf.append(" |");
                buf.append(args[argPos]);
                buf.append("| \n");
            }
        }
        
        return buf.toString();
    }

//================================================================    
	public static PropertyMngr create(String [] args, boolean bJvmArgMode)
	{
		return new PropertyMngr(args, bJvmArgMode);
	}
	
//    public static PropertyMngr createFake(boolean bJvmArgMode)
//    {
//        String [] fakeArgs = {
//                "-Dnavteq.lcms.job.id=abra", 
//                "-Dlcms.hdfs.rootdir=D:/tmp",
//                "-Dsupplier.id=99",
//                "-Dhdfs.conf.dir=D:/tmp",
//                "-step.name=CACHING",
////                "-step.name=sabra",
//                }; 
//        return create(fakeArgs, bJvmArgMode);
//    }
	
	private Map<String, String> argMap;
	
	private PropertyMngr(String [] args, boolean bJvmArgMode)
	{
		argMap = new HashMap<String, String>();
		for(String s : args)
		{
			String[] tokens = s.split("=");
			if(tokens.length != 2)
				continue;
			
			if(tokens[0].startsWith("-D"))       
			{
			    if(bJvmArgMode)
			    {
	                String flag = tokens[0].substring(2, tokens[0].length()); 
	                String value = tokens[1];
	                System.setProperty(flag, value);
			    }
			    else
			    {
                    String flag = tokens[0].substring(2, tokens[0].length()); 
                    String value = tokens[1];
                    argMap.put(flag, value);
			    }
			}
			else if(tokens[0].startsWith("-"))
            {
                String flag = tokens[0].substring(1, tokens[0].length()); 
                String value = tokens[1];
                argMap.put(flag, value);
            } 
		}
	}
	
	public String getValue(String flag)
	{
		return argMap.get(flag);
	}
	
    public boolean getValueAsBoolean(String flag)
    {
        String sVal = argMap.get(flag);
        Boolean BRet = new Boolean(sVal);
        return BRet;
    }

    /**
     * can throw NumberFormatException 
     * @param flag
     * @return
     */
    public long getValueAsLong(String flag)
    {
        return new Long(argMap.get(flag));
    }

	public String showArgMap()
	{
		StringBuilder buf = new StringBuilder();
		
		int len = argMap.size();
		if(len == 0)
		{
			buf.append("\tzero arguments");
		}
		else
		{
			for(String sKey : argMap.keySet())
			{
				buf.append("\t|" + sKey);
				buf.append("| - |");
				buf.append(argMap.get(sKey));
				buf.append("| \n");
			}
		}
		
		return buf.toString();
	}
	
	public Map<String, String> getSubMapByKeyPref(String pref)
	{
		Map<String, String> prefMap = new HashMap<String, String>();
		for(String key : argMap.keySet())
		{
			if(key.startsWith(pref))
			{
				String actKey = key.substring(pref.length(), key.length());
				prefMap.put(actKey, argMap.get(key));
			}
		}
		return prefMap;
	}
	
	@Override
	public String toString()
	{
	    return showArgMap();
	}

//================= JVM apguments ==========================

    public void extendWithJvmArgs(String ... keys)
    {
        for (String key : keys)
        {
            String value = System.getProperty(key);
            argMap.put(key, value);
        }
    }
    
    public String getJvmProperty(String key)
    {
        return System.getProperty(key);
    }
    
    public String showJvmProperties(String ... names)
    {
        StringBuilder buf = new StringBuilder();

        int len = names.length;
        if(len == 0)
        {
            buf.append("\tnone properties");
        }
        else
        {
            for(String name : names)
            {
                buf.append("\t ");
                buf.append(name);
                buf.append(" |");
                buf.append(System.getProperty(name));
                buf.append("| \n");
            }
        }
        
        return buf.toString();
    }
}
