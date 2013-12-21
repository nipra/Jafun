package com.practicalHadoop.uber;

import java.util.logging.Level;

import com.practicalHadoop.uber.util.PropertyMngr;

public class Launcher
{
	final private static String CLD_APP_START_CLASS = "appStart";
	final private static String CLD_PREF_PRG = "prefPkg";
	
	public static void main(String[] args) throws Exception
	{
        PropertyMngr propMngr = PropertyMngr.create(args, true);

        String sAppStart = propMngr.getValue(CLD_APP_START_CLASS);
		String sPrefPkg = propMngr.getValue(CLD_PREF_PRG);
		
		if(sAppStart == null || sAppStart.length() == 0)
			throw new RuntimeException("Launcher.main(): invalid class name to launch - " + sAppStart);
		
		CLDBuilder cldBuilder = new CLDBuilder();
		ClassLoaderNameFilter loadFilter = ClassLoaderNameFilter.create(sPrefPkg);
		TraceFilter traceFilter = TraceFilter.create();
		cldBuilder.buildPrefLoader(loadFilter, traceFilter);
		
		try
		{
			cldBuilder.invokeMain(sAppStart, args);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
}
