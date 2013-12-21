package com.practicalHadoop.uber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;

/**
 * 
 * Builds URRELoader for current uber jar
 *
 */
public class CLDBuilder
{
//	static public final String TEST_PKG_PATTERNS = "com.navteq";  
	static public final String TEST_PKG_PATTERNS = "aly.clsload";  
	
	public static void main(String [] args) throws Exception
	{
		CLDBuilder builder = new CLDBuilder();
		ClassLoaderNameFilter loadFilter = ClassLoaderNameFilter.create(TEST_PKG_PATTERNS);
		TraceFilter traceFilter = TraceFilter.create();
		builder.buildPrefLoader(loadFilter, traceFilter);
	}
	
	ClassLoader myCLD;
		
	public ClassLoader buildPrefLoader(ClassLoaderNameFilter loadFilter, TraceFilter traceFilter) throws Exception
	{
		CLUtil cldUtil = new CLUtil();
		cldUtil.unpackUberJar();
		
		ClassLoader extLoader = cldUtil.extendClassLoader(loadFilter, traceFilter);
		myCLD = extLoader;
		return extLoader;
	}

	public void invokeMain(String clsName, String[] args) throws Throwable
    {
		// just in case, if somebody will be using RMI or other indirect way to create objects
		// see http://docs.oracle.com/javase/jndi/tutorial/beyond/misc/classloader.html
		Thread.currentThread().setContextClassLoader(myCLD);
		
		Class<?> clazz = myCLD.loadClass(clsName);
		
		Method method = clazz.getMethod("main", new Class<?>[] { String[].class });
		
		boolean bValidModifiers = false;
		boolean bValidVoid = false;
		if (method != null)
		{
			method.setAccessible(true); // Disable IllegalAccessException
			int nModifiers = method.getModifiers(); 
			bValidModifiers = Modifier.isPublic(nModifiers)
			        && Modifier.isStatic(nModifiers);
			Class<?> clazzRet = method.getReturnType(); 
			bValidVoid = (clazzRet == void.class);
		}
		if (method == null || !bValidModifiers || !bValidVoid)
		{
			throw new NoSuchMethodException("The main() method in class \""
			        + clsName + "\" not found.");
		}
		try
		{
			method.invoke(null, (Object) args);
		}
		catch (InvocationTargetException e)
		{
			throw e.getTargetException();
		}
    }
}
