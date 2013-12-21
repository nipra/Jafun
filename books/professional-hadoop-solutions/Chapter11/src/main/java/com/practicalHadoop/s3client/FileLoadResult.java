package com.practicalHadoop.s3client;

import java.util.ArrayList;
import java.util.List;

public class FileLoadResult
{
	private int numberFilesRequested = 0;
	private int numberFilesLoaded = 0;
	private List<String> failedFileAbsolutePathList = new ArrayList<String>();
	private long startTime = 0;
	private long endTime = 0;
	
	public void clear()
	{
		startTime = 0;
		endTime = 0;
		numberFilesRequested = 0;
		numberFilesLoaded = 0;
		failedFileAbsolutePathList.clear();
	}
	
	public int getNumberFilesRequested()
	{
		return numberFilesRequested;
	}
	
	public void setNumberFilesRequested(int numberFilesRequested)
	{
		this.numberFilesRequested = numberFilesRequested;
	}
	
	public void addNumberFilesRequested(int numberFilesRequested)
	{
		this.numberFilesRequested += numberFilesRequested;
	}

	public int getNumberFilesLoaded()
	{
		return numberFilesLoaded;
	}
	
	public void setNumberFilesLoaded(int numberFilesLoaded)
	{
		this.numberFilesLoaded = numberFilesLoaded;
	}
	
	public void addNumberFilesLoaded(int numberFilesLoaded)
	{
		this.numberFilesLoaded += numberFilesLoaded;
	}

	public int getNumberFilesFailed()
	{
		return failedFileAbsolutePathList.size();
	}

	public long getExecutionTime()
	{
		return endTime - startTime;
	}

	public List<String> getFailedFileAbsolutePathList()
	{
		return failedFileAbsolutePathList;
	}
	
	public void setFailedFileAbsolutePathList(List<String> failedFileAbsolutePathList)
	{
		this.failedFileAbsolutePathList = failedFileAbsolutePathList;
	}
	
	public void addFile(String filePath)
	{
		if (!failedFileAbsolutePathList.contains(filePath))
		{
			failedFileAbsolutePathList.add(filePath);
		}
	}
	
	public long getStartTime()
	{
		return startTime;
	}
	
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	
	public long getEndTime()
	{
		return endTime;
	}
	
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}
	
	public void printResult()
	{
		System.out.println("Execution time: " + getExecutionTime() / 1000. + "sec");
		System.out.println("Number files loaded: " + getNumberFilesLoaded());
		System.out.println("Number files failed: " + getNumberFilesFailed());
		
		if (getNumberFilesFailed() > 0)
		{
			System.out.println("Files which were unable to load:");
			for (String name : getFailedFileAbsolutePathList())
			{
				System.out.println(name);
			}
		}
	}
}
