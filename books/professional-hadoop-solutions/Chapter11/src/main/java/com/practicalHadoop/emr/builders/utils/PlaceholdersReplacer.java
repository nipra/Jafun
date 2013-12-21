package com.practicalHadoop.emr.builders.utils;

import java.util.Properties;

public final class PlaceholdersReplacer {
	
	// hide default constructor
	private PlaceholdersReplacer() 
	{		
	}
	
	//Method can replace placeholders like {placeholder} or ${placeholder}.
	//Replacement map should be populated without special characters. Example: replacement.put("placeholder", "my value");
	public static void replace(Properties replacement, Properties properties)
	{
		for(String key : properties.stringPropertyNames())
		{
			String value = properties.getProperty(key);
			int shift = 0;	
			StringBuilder result = new StringBuilder();
			for (int index = 0; index < value.length(); index++)
			{
				char ch = value.charAt(index);
				
				if(ch == '$')
				{
					shift = 1;
				}				
				else if (ch == '{')
				{					
					int openBraceIndex = index;
					
					while (++index < value.length() && value.charAt(index) != '}')
					{
						;
					}
					
					if (index == value.length()) //no close brace
					{
						result.append(value.substring(openBraceIndex - shift, index));
						break;
					}
					
					String placeholder = value.substring(openBraceIndex + 1, index);
					if (replacement.containsKey(placeholder)) //placeholder is defined in map
					{
						result.append(replacement.get(placeholder));
					}
					else
					{
						result.append(value.substring(openBraceIndex - shift, index + 1));
					}
					
					shift = 0;
				}
				else
				{
					if(shift > 0)
					{
						shift = 0;
						result.append(value.charAt(index - 1));
					}
					result.append(ch);
				}
			}
			
			properties.setProperty(key, result.toString());
		}
	}
}
