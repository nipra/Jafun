package com.packtpub.java7.concurrency.chapter4.recipe12.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.packtpub.java7.concurrency.chapter4.recipe12.task.RejectedTaskController;
import com.packtpub.java7.concurrency.chapter4.recipe12.task.Task;


/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Create the controller for the Rejected tasks
		RejectedTaskController controller=new RejectedTaskController();
		// Create the executor and establish the controller for the Rejected tasks
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		executor.setRejectedExecutionHandler(controller);
		
		// Lauch three tasks
		System.out.printf("Main: Starting.\n");
		for (int i=0; i<3; i++) {
			Task task=new Task("Task"+i);
			executor.submit(task);
		}
		
		// Shutdown the executor
		System.out.printf("Main: Shuting down the Executor.\n");
		executor.shutdown();

		// Send another task
		System.out.printf("Main: Sending another Task.\n");
		Task task=new Task("RejectedTask");
		executor.submit(task);
		
		// The program ends
		System.out.printf("Main: End.\n");
		
	}

}
