package com.packtpub.java7.concurrency.chapter2.recipe2.core;

import java.util.Date;

import com.packtpub.java7.concurrency.chapter2.recipe2.task.BuildStats;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.Sensor1;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.Sensor2;

/**
 * Main class of the example. Creates an object with the statistics of the
 * building and executes two threads that simulates two sensors in the building
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create a new object for the statistics
		BuildStats stats=new BuildStats();

		// Create a Sensor1 object and a Thread to run it
		Sensor1 sensor1=new Sensor1(stats);
		Thread thread1=new Thread(sensor1,"Sensor 1");

		// Create a Sensor 2 object and a Thread to run it
		Sensor2 sensor2=new Sensor2(stats);
		Thread thread2=new Thread(sensor2,"Sensor 2");
		
		// Get the actual time
		Date date1=new Date();
		
		//Starts the threads
		thread1.start();
		thread2.start();
		
		try {
			// Wait for the finalization of the threads
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Get the actual time and print the execution time
		Date date2=new Date();
		stats.printStats();
		System.out.println("Execution Time: "+((date2.getTime()-date1.getTime())/1000));

	}

}
