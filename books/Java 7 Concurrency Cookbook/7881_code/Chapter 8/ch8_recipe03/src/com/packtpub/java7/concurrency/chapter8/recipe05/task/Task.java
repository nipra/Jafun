package com.packtpub.java7.concurrency.chapter8.recipe05.task;

import java.util.concurrent.TimeUnit;

/**
 * Task implemented to log data about an Executor 
 *
 */
public class Task implements Runnable {

	/**
	 * Number of milliseconds this task is going to sleep the thread
	 */
	private long milliseconds;
	
	/**
	 * Constructor of the task. Initializes its attributes
	 * @param milliseconds Number of milliseconds this task is going to sleep the thread
	 */
	public Task (long milliseconds) {
		this.milliseconds=milliseconds;
	}
	
	/**
	 * Main method of the task. Sleep the thread the number of millisecons specified by
	 * the milliseconds attribute.
	 */
	@Override
	public void run() {
		
		System.out.printf("%s: Begin\n",Thread.currentThread().getName());
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: End\n",Thread.currentThread().getName());
		
	}

}
