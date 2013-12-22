package com.packtpub.java7.concurrency.chapter1.recipe12.core;

import com.packtpub.java7.concurrency.chapter1.recipe12.factory.MyThreadFactory;
import com.packtpub.java7.concurrency.chapter1.recipe12.task.Task;

/**
 * Main class of the example. Creates a Thread factory and creates ten 
 * Thread objects using that Factory 
 *
 */
public class Main {

	/**
	 * Main method of the example. Creates a Thread factory and creates 
	 * ten Thread objects using that Factory
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates the factory
		MyThreadFactory factory=new MyThreadFactory("MyThreadFactory");
		// Creates a task
		Task task=new Task();
		Thread thread;
		
		// Creates and starts ten Thread objects
		System.out.printf("Starting the Threads\n");
		for (int i=0; i<10; i++){
			thread=factory.newThread(task);
			thread.start();
		}
		// Prints the statistics of the ThreadFactory to the console
		System.out.printf("Factory stats:\n");
		System.out.printf("%s\n",factory.getStats());
		
	}

}
