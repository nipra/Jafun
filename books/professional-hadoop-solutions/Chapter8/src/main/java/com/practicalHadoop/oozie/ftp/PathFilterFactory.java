package com.practicalHadoop.oozie.ftp;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.PathFilter;

public class PathFilterFactory
{
	public enum PathFilterEnum { FILE, GZIP, FILE_NOT_EMPTY }
	
	public static PathChainFilter create(PathFilterEnum filterEnum, FileSystem fileSys, PathChainFilter parent)
	{
		PathChainFilter filter = null;

		switch(filterEnum)
		{
			case FILE:
				filter = new FilePathFilter(fileSys, parent);
				break;
				
			case GZIP:
				filter = new GzipPathFilter(fileSys, parent);
				break;
				
			case FILE_NOT_EMPTY:
				filter = new FileNotEmptyFilter(fileSys, parent);
				break;
				
			default:
				throw new IllegalArgumentException("PathFilterFactory.create(): invalid arg");
		}
		
		return filter;
	}
}
