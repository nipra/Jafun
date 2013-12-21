package com.practicalHadoop.s3copytool;

import com.practicalHadoop.queue.HdQueue;
import java.util.List;

public class CacheShutdownHook extends Thread
{
	HdQueue queue = null; // Use when cache write mapper/reducer failed to dequeue back keys.
	List<String> keys = null;

	public CacheShutdownHook(HdQueue queue, List<String> keys){
		this.keys = keys;
		this.queue = queue;
	}


	 public void attachShutDownHook(){
		Runtime.getRuntime().addShutdownHook(this);
	}

	@Override
	public void run()
	{
		try{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			// Intentionally left blank.
		}
		enqueueString();
	}

	protected void enqueueString()
	{
		if ((queue != null) && (keys != null))
		{
			// For cases in which mapper or reducer failed, enqueue back unfinished keys.
			try
			{
				queue.enqueueString(keys);
			}
			catch (Exception e)
			{
				// Program shutdown failed, not much can be done.
				throw new RuntimeException("Enqueue into " + queue.getQname() + " failed", e);
			}
		}
	}
}
