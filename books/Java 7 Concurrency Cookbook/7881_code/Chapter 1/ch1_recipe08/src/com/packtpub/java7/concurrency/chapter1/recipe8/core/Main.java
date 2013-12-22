package com.packtpub.java7.concurrency.chapter1.recipe8.core;

import com.packtpub.java7.concurrency.chapter1.recipe8.handler.ExceptionHandler;
import com.packtpub.java7.concurrency.chapter1.recipe8.task.Task;

/**
 * Main class of the example. Initialize a Thread to process the uncaught
 * exceptions and starts a Task object that always throws an exception 
 *
 */
public class Main {

	/**
	 * Main method of the example. Initialize a Thread to process the 
	 * uncaught exceptions and starts a Task object that always throws an
	 * exception 
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates the Task
		Task task=new Task();
		// Creates the Thread
		Thread thread=new Thread(task);
		// Sets de uncaugh exceptio handler
		thread.setUncaughtExceptionHandler(new ExceptionHandler());
		// Starts the Thread
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Thread has finished\n");

	}

}
