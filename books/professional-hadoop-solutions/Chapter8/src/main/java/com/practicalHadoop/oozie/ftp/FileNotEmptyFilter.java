package com.practicalHadoop.oozie.ftp;

import java.io.IOException;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 
 * Checks that file is not empty
 * Relies on caller to make sure that path in accept() points to existing file.
 *
 */
public class FileNotEmptyFilter extends PathChainFilter
{
	FileNotEmptyFilter(FileSystem fileSys, PathChainFilter parent)
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
			if(parent != null && parent.accept(path) == false)
			{
				ret = false;
				break __block;
			}
			
			FileStatus fStatus = fileSys.getFileStatus(path);
			long len = fStatus.getLen();
			if(len > 0)
				ret = true;
			else
				ret = false;
		}
		catch(IOException ex)
		{
			throw new RuntimeException(
			        "FileNotEmptyFilter.getValidFiles(): IOException in isFile(): "
			                + path, ex);
		}
		return ret;
	}
}
