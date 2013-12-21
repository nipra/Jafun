package com.practicalHadoop.oozie.ftp;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class GzipPathFilter extends PathChainFilter
{
	GzipPathFilter(FileSystem fileSys, PathChainFilter parent)
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
				
				String fileShortName = path.getName();
				if(fileShortName.startsWith("_") || fileShortName.startsWith("."))
				{
					System.out.println("... rejected: " + path);
					ret = false;
					break __block;
				}
				
				System.out.println("... accepted: " + path);
				ret = true;
				break __block;
			}
			else
			{
				System.out.println("... rejected: " + path);
				ret = false;
				break __block;
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(
			        "GzipPathFilter.getValidFiles(): IOException in isFile(): "
			                + path, e);
		}
		return ret;
	}
}
