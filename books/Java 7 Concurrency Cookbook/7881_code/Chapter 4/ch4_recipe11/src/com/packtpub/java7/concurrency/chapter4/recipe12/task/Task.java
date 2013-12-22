package com.packtpub.java7.concurrency.chapter4.recipe12.task;

import java.util.concurrent.TimeUnit;

/**
 * This class implements a task executed in this example. It only waits
 * a random perior of time
 *
 */
public class Task implements Runnable{

	/**
	 * Name of the task
	 */
	private String name;

	/**
	 * Constructor of the class. It initializes the attributes of the class
	 * @param name The name of the task
	 */
	public Task(String name){
		this.name=name;
	}
	
	/**
	 * Main method of the task. Waits a random period of time
	 */
	@Override
	public void run() {
		System.out.printf("Task %s: Starting\n",name);
		try {
			Long duration=(long)(Math.random()*10);
			System.out.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n",name,duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		System.out.printf("Task %s: Ending\n",name);
	}

	/**
	 * Returns the name of the task
	 */
	public String toString() {
		return name;
	}
	
}
