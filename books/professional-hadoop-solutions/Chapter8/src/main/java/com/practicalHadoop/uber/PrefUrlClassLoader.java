package com.practicalHadoop.uber;

import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrefUrlClassLoader extends URLClassLoader
{
    private static final Logger logger = LoggerFactory.getLogger("WfStarter");
    
	private final TraceFilter traceFilter;
	private final ClassLoaderNameFilter loadFilter;
	
	public PrefUrlClassLoader(URL[] urls, ClassLoaderNameFilter loadFilter, TraceFilter traceFilter)
    {
	    super(urls);
	    this.loadFilter = loadFilter;
	    this.traceFilter = traceFilter;
    }

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException
	{
		boolean bTraceClass = false;
		
		if(traceFilter != null)
			bTraceClass = traceFilter.isTracing(name);
		
		Class<?> cls = null;
		
		/*
		 * for classes from the root of uber jar: they were already loaded with parent CL, 
		 * but we want them with PrefUrlClassLoader, to create "children" with new 
		 * by the same PrefUrlClassLoader
		 */
		if(loadFilter.internalLoad(name))
		{
			cls = findLoadedClass(name);
			if(cls == null)
			{
				try
				{
					cls = findClass(name);
				}
				catch(ClassNotFoundException clnf)
				{
                    logger.debug("~~~~ B1: NOT FOUND"); 
					// never mind, it will try now the parent class loader
				}
			}
			else
			{
				if(bTraceClass)
				    logger.debug("~~~~ B1: findLoadedClass() FOUND"); 
			}
		}
		
		if(cls == null)
		{
			/* 
			 * this will check both super - URLClassLoader, 
			 * and parent class loader 
			 */
			cls = super.findLoadedClass(name);				 
			if(cls == null)
			{
				try
				{
					cls = getParent().loadClass(name);
				}
				catch(ClassNotFoundException clnf)
				{
				    logger.debug("~~~~ B2: parent loadClass() MISSED");
					// never mind, it will try now the general base class again without filters
				}
			}
			else
			{
				logger.debug("~~~~ B2: super.findLoadedClass() FOUND"); 
			}
			
		}
		
		/*
		 * load classes in internal jars that are not provided by parent class loader
		 * and not selected by filter
		 * those classes will be searched now as in "delegation pattern" mode
		 * but if such a class is already loaded, it will be found in prev.case, 
		 * in super.findLoadedClass(name)
		 */
		if(cls == null)
		{
			try
			{
				/*
				 * this is actually super.findClass(name);
				 */
				cls = findClass(name);		
			}
			catch(ClassNotFoundException clnf)   // now we have a problem!
			{
                logger.debug("~~~~ B3: exception findClass(): MISSED"); 
				throw new ClassNotFoundException("", clnf);
			}
		}
		return cls;
	}
}
