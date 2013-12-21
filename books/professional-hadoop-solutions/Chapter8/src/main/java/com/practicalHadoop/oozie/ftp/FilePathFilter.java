package com.practicalHadoop.oozie.ftp;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Filter checks that the submitted path specify a file, not a directory
 */
public class FilePathFilter extends PathChainFilter
{
	FilePathFilter(FileSystem fileSys, PathChainFilter parent)
	{
		super(parent);
		this.fileSys = fileSys;
	}
	
	public boolean accept(Path path)
	{
		boolean ret = false;
__block:		
		try
		{
			if (fileSys.isFile(path))
			{
				if(parent != null && parent.accept(path) == false)
				{
					ret = false;
					break __block;
				}
				
				System.out.println("... accepted: " + path);
				ret = true;
			}
			else
			{
				System.out.println("... rejected: " + path);
				ret = false;
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(
			        "FilePathFilter.getValidFiles(): IOException in isFile(): "
			                + path, ex);
		}
		return ret;
	}
}
