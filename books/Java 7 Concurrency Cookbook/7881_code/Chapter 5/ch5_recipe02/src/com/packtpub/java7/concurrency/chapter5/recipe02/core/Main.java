package com.packtpub.java7.concurrency.chapter5.recipe02.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter5.recipe02.task.DocumentTask;
import com.packtpub.java7.concurrency.chapter5.recipe02.utils.DocumentMock;

/**
 * Main class of the example. 
 */
public class Main {

	/**
	 * Main method of the class
	 */
	public static void main(String[] args) {
		
		// Generate a document with 100 lines and 1000 words per line
		DocumentMock mock=new DocumentMock();
		String[][] document=mock.generateDocument(100, 1000, "the");
	
		// Create a DocumentTask
		DocumentTask task=new DocumentTask(document, 0, 100, "the");
		
		// Create a ForkJoinPool
		ForkJoinPool pool=new ForkJoinPool();
		
		// Execute the Task
		pool.execute(task);
		
		// Write statistics about the pool
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n",pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n",pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n",pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n",pool.getStealCount());
			System.out.printf("******************************************\n");

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} while (!task.isDone());

		// Shutdown the pool
		pool.shutdown();
		
		// Wait for the finalization of the tasks
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Write the results of the tasks
		try {
			System.out.printf("Main: The word appears %d in the document",task.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
