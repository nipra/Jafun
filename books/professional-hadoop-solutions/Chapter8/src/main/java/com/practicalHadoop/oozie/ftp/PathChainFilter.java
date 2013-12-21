package com.practicalHadoop.oozie.ftp;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.PathFilter;

abstract public class PathChainFilter implements PathFilter
{
	PathChainFilter parent;
	FileSystem fileSys;

	PathChainFilter(PathChainFilter parent)
	{
		this.parent = parent;
	}
	
//	public boolean accept(Path path);

	PathFilter getParent()
	{
		return parent;
	}
}
