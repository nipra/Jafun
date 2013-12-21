package com.practicalHadoop.oozie.ftp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.GzipCodec;

public class LocalHelper
{
//	static final String sWinFtpRoot = "D:/tmp/";

	public static boolean isOnWindows()
	{
		String osName = System.getProperty("os.name");
		boolean bRunOnWindows = (osName.contains("Windows") ? true : false);
		return bRunOnWindows;
	}

	static InputStream openStdFileInputStream(String path) throws IOException
	{
		return new FileInputStream(path);
	}

	/**
	 * assumes the following file structure to do gzip:
	 * --ROOT
	 * ----USA
	 * --------usa_file
	 * ----UK
	 * --------uk_file
	 * ----FRANCE
	 * --------france-file
	 *      
	 *  all gziped files are put in one flat dir    
	 */
	public static List<Path> calculateGZPath(String local, FileSystem fileSys, PathChainFilter filter) throws IOException
	{
		System.out.println("LocalHelperl.calculateGZPath() on entry: local - " + local);
		
		Path pPathLocal = new Path(local);
		if(! fileSys.exists(pPathLocal))
			return null;
		
		List<Path> paths = new ArrayList<Path>();
		if (fileSys.isFile(pPathLocal))
		{
			String errMsg = "LocalHelper.calculateGZPath(): invalid data a file - expected a directory: " + pPathLocal;
			System.out.println(errMsg);
			throw new IllegalArgumentException(errMsg);
		}
		
		FileStatus[] statusArrDir = fileSys.listStatus(pPathLocal);
		for (FileStatus status : statusArrDir)
		{
			if(!status.isDir())
			{
				System.out.println(" ... ignoring not a dir: " + status.getPath());
				continue;
			}
			
			// second level - directory
			FileStatus[] statusArrFiles = fileSys.listStatus(status.getPath(), filter);
			for(FileStatus status2 : statusArrFiles)
			{
				if(status2.isDir())
				{
					System.out.println(" ... ignoring not a file: " + status.getPath());
					continue;
				}
				
				paths.add(status2.getPath());
			}
		}
			
		System.out.println("LocalHelperl.calculateGZPath() on exit: paths contins: - " + paths.size());
		return paths;
	}

	/**
	 *
	 *  Just takes all files from the specified directory (local) that pass the specified filters 
	 */
	public static List<Path> calculateFtpPath(String local, FileSystem fileSys, PathChainFilter filter) throws IOException
	{
		System.out.println("LocalHelperl.calculateFTPPath() on entry: local - " + local);
		
		Path pPathLocal = new Path(local);
		if(! fileSys.exists(pPathLocal))
			return null;
		
		List<Path> paths = new ArrayList<Path>();
		if (fileSys.isFile(pPathLocal))
		{
			String errMsg = "LocalHelper.calculateFtpPath(): invalid data a file - expected a directory: " + pPathLocal;
			System.out.println(errMsg);
			throw new IllegalArgumentException(errMsg);
		}

		FileStatus[] statusArrFiles = fileSys.listStatus(pPathLocal, filter);
		for (FileStatus status : statusArrFiles)
			paths.add(status.getPath());
		
		System.out.println("LocalHelperl.calculateFTPPath() on exit");
		return paths;
	}
	
	
	public static FSDataInputStream openFSInputStream(Path pFilePath) throws IOException
	{
		Configuration conf = new Configuration();
		FileSystem fileSys = pFilePath.getFileSystem(conf);

		boolean bFileExists = fileSys.exists(pFilePath);
		if (bFileExists == false)
			throw new IOException(
			        "InputStreamUtil.openFSDataInputStream(): file does not exists: "
			                + pFilePath);

		FSDataInputStream fsInput = fileSys.open(pFilePath);
		return fsInput;
	}

	static CompressionInputStream openGZipFSInputStream(String sFile) throws IOException
	{
		Configuration conf = new Configuration();
		Path pFilePath = new Path(sFile);
		FileSystem fileSys = pFilePath.getFileSystem(conf);

		boolean bFileExists = fileSys.exists(pFilePath);
		if (bFileExists == false)
			throw new IOException(
			        "InputStreamUtil.openFSDataInputStream(): file does not exists: " + sFile);

		FSDataInputStream fsInput = fileSys.open(pFilePath);
		GzipCodec gzip = new GzipCodec();
		CompressionInputStream gzipInput = gzip.createInputStream(fsInput);

		return gzipInput;
	}
}
