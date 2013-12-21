package com.practicalHadoop.uber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CLUtil
{

	String sJarURL;
	String sJarDir;
	List<String> internalJarsURLList;

	public CLUtil()
	{
		internalJarsURLList = new ArrayList<String>();
	}

	String findContainingJar(Class<?> clazz)
	{
		ClassLoader loader = clazz.getClassLoader();
		String clsFile = clazz.getName().replaceAll("\\.", "/")
		        + ".class";
		try
		{
			for (Enumeration<URL> itr = loader.getResources(clsFile); itr
			        .hasMoreElements();)
			{
				URL url = itr.nextElement();
//				DumpLog.dump(Level.FINE, "** CLUtil.findContainingJar(): url " + url);
				if ("jar".equals(url.getProtocol()))
				{
					String sWorkURI = url.getPath();
//					DumpLog.dump(Level.FINE, "**  url path " + sWorkURI);
					if (sWorkURI.startsWith("file:"))
					{
						sWorkURI = sWorkURI.substring("file:".length());
//						DumpLog.dump(Level.FINE, "**  file out: " + sWorkURI);
					}
					// URLDecoder is a misnamed class, since it actually decodes
					// x-www-form-urlencoded MIME type rather than actual
					// URL encoding (which the file path has). Therefore it
					// would
					// decode +s to ' 's which is incorrect (spaces are actually
					// either unencoded or encoded as "%20"). Replace +s first,
					// so
					// that they are kept sacred during the decoding process.
					sWorkURI = sWorkURI.replaceAll("\\+", "%2B");
//					DumpLog.dump(Level.FINE, "** replace : " + sWorkURI);
					sWorkURI = URLDecoder.decode(sWorkURI, "UTF-8");
//					DumpLog.dump(Level.FINE, "**  decode: " + sWorkURI);

					sWorkURI = sWorkURI.replaceAll("!.*$", "");
//					DumpLog.dump(Level.FINE, "**  replace: " + sWorkURI);
					
					int lastSlash = sWorkURI.lastIndexOf("/");
					sJarDir = sWorkURI.substring(0, lastSlash);
					
					sJarURL = sWorkURI;
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return sJarURL;
	}

	void testCanWrite() throws IOException
	{
		File newFile = new File(sJarDir + "/TestNewFile.aly");
		FileWriter fWriter = new FileWriter(newFile);
		fWriter.write("abra-cadabra");
		fWriter.close();
	}

	void unpackUberJar() throws IOException
	{
		unpackUberJar(sJarDir, sJarURL);
	}
	
	
	void unpackUberJar(String sJarDir, String sJarURL) throws IOException
	{
		File fJarDir = new File(sJarDir);
		File fJarFile = new File(sJarURL);
		JarFile jarFile = new JarFile(fJarFile);
		try
		{
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements())
			{
				JarEntry jarEntry = (JarEntry) entries.nextElement();
				String entryName = jarEntry.getName();

				if (!jarEntry.isDirectory() && entryName.endsWith(".jar"))
				{
//					DumpLog.dump(Level.FINE, " ** entryName - " + entryName);
					extractJar(fJarDir, jarFile, jarEntry);
					String intJarUrl = sJarDir + "/" + jarEntry.getName(); 
					unpackUberJar(sJarDir, intJarUrl);
				}
			}
		}
		finally
		{
			jarFile.close();
		}
	}

	ClassLoader extendClassLoader(ClassLoaderNameFilter loadFilter, TraceFilter traceFilter) throws MalformedURLException,
	        ClassNotFoundException
	{
		List<URL> urlJarList = new ArrayList<URL>();

		File fEntry = new File(sJarURL);
		URL urlEntry = fEntry.toURI().toURL();
		urlJarList.add(urlEntry);
		
		for (String sEntry : internalJarsURLList)
		{
			String sFullName = sJarDir + "/" + sEntry;
//			DumpLog.dump(Level.FINE, "** sFullName - " + sFullName);
			fEntry = new File(sFullName);
			urlEntry = fEntry.toURI().toURL();
//			DumpLog.dump(Level.FINE, "** urlEntry - " + urlEntry);
			urlJarList.add(urlEntry);
		}

		URL[] jarUrlArr = urlJarList.toArray(new URL[0]);
		PrefUrlClassLoader prefUrlCLD = new PrefUrlClassLoader(jarUrlArr, loadFilter, traceFilter);
		return prefUrlCLD;
	}

	private void ensureDirectory(File dir) throws IOException
	{
		if (!dir.mkdirs() && !dir.isDirectory())
		{
			throw new IOException("Mkdirs failed to create " + dir.toString());
		}
	}

	private void extractJar(File fJarDir, JarFile jar, JarEntry entry) throws IOException
	{
		String entryName = entry.getName();
		internalJarsURLList.add(entryName);
		InputStream in = null;
		try
		{
			in = jar.getInputStream(entry);
			File file = new File(fJarDir, entryName);
			ensureDirectory(file.getParentFile());
			OutputStream out = new FileOutputStream(file);
			try
			{
				copyBytes(in, out, 8192, false);
			}
			finally
			{
				out.close();
			}
		}
		finally
		{
			in.close();
		}
	}
	
	private void copyBytes(InputStream in, OutputStream out, int buffSize,
	        boolean close) throws IOException
	{
		byte buf[] = new byte[buffSize];
		try
		{
			int bytesRead = in.read(buf);
			while (bytesRead >= 0)
			{
				out.write(buf, 0, bytesRead);
				bytesRead = in.read(buf);
			}
		}
		finally
		{
			if (close)
			{
				out.close();
				in.close();
			}
		}
	}
}
